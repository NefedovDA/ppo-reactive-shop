package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty

data class JsonUserCredentials(
    @JsonProperty("login")
    val login: String,

    @JsonProperty("password")
    val password: String,
)