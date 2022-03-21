package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.diamant.rabbit.reactiveShop.domain.Role

data class JsonRole(
    @JsonProperty("role")
    val role: Role
)