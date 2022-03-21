package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty

data class JsonToken(
    @JsonProperty("token")
    val token: String
)