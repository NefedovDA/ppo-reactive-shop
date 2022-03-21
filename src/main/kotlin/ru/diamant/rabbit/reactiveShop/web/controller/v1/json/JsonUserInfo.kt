package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.diamant.rabbit.reactiveShop.domain.Role
import ru.diamant.rabbit.reactiveShop.domain.User
import ru.diamant.rabbit.reactiveShop.domain.role

data class JsonUserInfo(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("login")
    val login: String,

    @JsonProperty("role")
    val role: Role,
) {
    companion object {
        operator fun invoke(user: User): JsonUserInfo =
            JsonUserInfo(
                id = requireNotNull(user.id) { "Non DB user?" },
                login = user.login,
                role = user.role
            )
    }
}