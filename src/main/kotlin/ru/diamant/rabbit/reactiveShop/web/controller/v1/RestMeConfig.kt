package ru.diamant.rabbit.reactiveShop.web.controller.v1

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.UserConfig
import ru.diamant.rabbit.reactiveShop.domain.defaultCurrency
import ru.diamant.rabbit.reactiveShop.repository.UserConfigRepository
import ru.diamant.rabbit.reactiveShop.security.user
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonUserConfigInfo
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonUserConfigUpdate

@RestController
@RequestMapping("/api/v1/me/config")
class RestMeConfig(
    private val userConfigRepository: UserConfigRepository
) {
    @GetMapping
    fun getConfig(authentication: Authentication): Mono<JsonUserConfigInfo> =
        userConfigRepository.findByUserId(checkNotNull(authentication.user.id) { "Non DB User?" })
            .map { JsonUserConfigInfo(it.defaultCurrency) }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))

    @PostMapping
    fun createConfig(authentication: Authentication, @RequestBody jsonUserConfigInfo: JsonUserConfigInfo): Mono<Void> =
        userConfigRepository
            .save(
                UserConfig(
                    id = null,
                    userId = checkNotNull(authentication.user.id) { "Non DB User?" },
                    defaultCurrencyName = jsonUserConfigInfo.defaultCurrency.name
                )
            )
            .onErrorResume(DataIntegrityViolationException::class.java) {
                Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user config", it))
            }
            .then()

    @PutMapping
    fun updateConfig(
        authentication: Authentication,
        @RequestBody jsonUserConfigUpdate: JsonUserConfigUpdate
    ): Mono<Void> =
        userConfigRepository
            .findByUserId(checkNotNull(authentication.user.id) { "Non DB User?" })
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap {
                userConfigRepository.save(
                    it.copy(
                        defaultCurrencyName = jsonUserConfigUpdate.defaultCurrency?.name ?: it.defaultCurrencyName
                    )
                )
            }
            .then()

    @DeleteMapping
    fun cleanConfig(authentication: Authentication): Mono<Void> =
        userConfigRepository
            .findByUserId(checkNotNull(authentication.user.id) { "Non DB User?" })
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap { userConfigRepository.delete(it) }
}