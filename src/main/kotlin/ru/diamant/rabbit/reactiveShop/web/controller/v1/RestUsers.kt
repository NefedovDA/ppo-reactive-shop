package ru.diamant.rabbit.reactiveShop.web.controller.v1

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Role
import ru.diamant.rabbit.reactiveShop.domain.role
import ru.diamant.rabbit.reactiveShop.repository.UserRepository
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonRole
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonUserInfo

@RestController
@RequestMapping("/api/v1/users")
class RestUsers(
    private val userRepository: UserRepository,
) {
    @GetMapping
    fun getAll(): Flux<JsonUserInfo> =
        userRepository
            .findAll()
            .map { JsonUserInfo(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Mono<JsonUserInfo> =
        userRepository
            .findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map { JsonUserInfo(it) }

    @PostMapping("/{id}/role")
    fun updateRole(@PathVariable("id") id: Long, @RequestBody jsonRole: JsonRole): Mono<Void> =
        userRepository
            .findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .filter { it.role != Role.OWNER }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN)))
            .flatMap { userRepository.save(it.copy(roleName = jsonRole.role.name)) }
            .then()
}