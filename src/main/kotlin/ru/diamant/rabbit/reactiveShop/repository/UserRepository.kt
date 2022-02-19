package ru.diamant.rabbit.reactiveShop.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.User

@Repository
interface UserRepository : ReactiveCrudRepository<User, Long> {
    fun findByEmail(email: String): Mono<User>
}