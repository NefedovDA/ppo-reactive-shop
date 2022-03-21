package ru.diamant.rabbit.reactiveShop.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.diamant.rabbit.reactiveShop.domain.Product

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, Long> {
    fun findByOwnerId(ownerId: Long): Flux<Product>
}