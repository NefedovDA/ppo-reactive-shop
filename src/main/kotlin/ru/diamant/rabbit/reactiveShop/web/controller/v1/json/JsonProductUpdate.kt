package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty

data class JsonProductUpdate(
    @JsonProperty("title")
    val title: String?,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("price")
    val price: JsonPrice?,
)
