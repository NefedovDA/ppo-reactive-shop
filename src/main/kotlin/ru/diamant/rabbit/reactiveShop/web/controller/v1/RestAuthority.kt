package ru.diamant.rabbit.reactiveShop.web.controller.v1

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Role
import ru.diamant.rabbit.reactiveShop.domain.User
import ru.diamant.rabbit.reactiveShop.repository.UserRepository
import ru.diamant.rabbit.reactiveShop.security.jwt.JwtUtils
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonToken
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonUserCredentials

@RestController
@RequestMapping("/api/v1/auth")
class RestAuthority(
    private val jwtUtils: JwtUtils,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("create")
    fun create(@RequestBody userCredentials: JsonUserCredentials): Mono<JsonToken> =
        userRepository
            .save(
                User(
                    id = null,
                    login = userCredentials.login,
                    password = passwordEncoder.encode(userCredentials.password),
                    roleName = Role.USER.name
                )
            )
            .onErrorResume(DataIntegrityViolationException::class.java) {
                Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user", it))
            }
            .map { JsonToken(jwtUtils.createToken(it)) }


    @PostMapping("token")
    fun token(@RequestBody userCredentials: JsonUserCredentials): Mono<JsonToken> =
        userRepository
            .findByLogin(userCredentials.login)
            .filter { it.password == passwordEncoder.encode(userCredentials.password) }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED)))
            .map { JsonToken(jwtUtils.createToken(it)) }
}