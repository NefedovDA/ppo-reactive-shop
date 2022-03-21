package ru.diamant.rabbit.reactiveShop.web.controller.v1

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Authority
import ru.diamant.rabbit.reactiveShop.domain.Product
import ru.diamant.rabbit.reactiveShop.repository.ProductRepository
import ru.diamant.rabbit.reactiveShop.security.user
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonProductInfo
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonProductNew
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonProductUpdate

@RestController
@RequestMapping("/api/v1/products")
class RestProducts(
    private val productRepository: ProductRepository
) {
    @GetMapping
    fun getAll(): Flux<JsonProductInfo> =
        productRepository
            .findAll()
            .map { JsonProductInfo(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Mono<JsonProductInfo> =
        productRepository
            .findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map { JsonProductInfo(it) }

    @PostMapping
    fun createProduct(
        authentication: Authentication,
        @RequestBody jsonProductNew: JsonProductNew,
    ): Mono<JsonProductInfo> =
        productRepository
            .save(
                Product(
                    id = null,
                    title = jsonProductNew.title,
                    description = jsonProductNew.description,
                    price_amount = jsonProductNew.price.amount,
                    price_currency = jsonProductNew.price.currency.name,
                    ownerId = checkNotNull(authentication.user.id) { "Non BD user?" }
                )
            )
            .onErrorResume(DataIntegrityViolationException::class.java) {
                Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create product", it))
            }
            .map { JsonProductInfo(it) }

    @PutMapping("/{id}")
    fun updateProduct(
        authentication: Authentication,
        @PathVariable("id") id: Long,
        @RequestBody jsonProductUpdate: JsonProductUpdate,
    ): Mono<Void> =
        productRepository
            .findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .filter { it.ownerId == authentication.user.id }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN)))
            .flatMap {
                productRepository.save(
                    it.copy(
                        title = jsonProductUpdate.title ?: it.title,
                        description = jsonProductUpdate.description ?: it.description,
                        price_amount = jsonProductUpdate.price?.amount ?: it.price_amount,
                        price_currency = jsonProductUpdate.price?.currency?.name ?: it.price_currency,
                    )
                )
            }
            .then()

    @DeleteMapping("/{id}")
    fun deleteProduct(
        authentication: Authentication,
        @PathVariable("id") id: Long,
    ): Mono<Void> =
        productRepository
            .findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .filter {
                SimpleGrantedAuthority(Authority.ADMIN) in authentication.authorities ||
                        it.ownerId == authentication.user.id
            }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN)))
            .flatMap { productRepository.delete(it) }
}