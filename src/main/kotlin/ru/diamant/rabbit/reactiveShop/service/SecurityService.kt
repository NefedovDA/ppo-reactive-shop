package ru.diamant.rabbit.reactiveShop.service

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.details
import ru.diamant.rabbit.reactiveShop.repository.UserRepository

@Service
class SecurityService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> =
        userRepository
            .findByEmail(username)
            .map { it.details }
}