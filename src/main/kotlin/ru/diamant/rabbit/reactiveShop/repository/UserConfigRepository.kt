package ru.diamant.rabbit.reactiveShop.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.UserConfig

interface UserConfigRepository : ReactiveCrudRepository<UserConfig, Long> {
    fun findByUserId(userId: Long): Mono<UserConfig>
}