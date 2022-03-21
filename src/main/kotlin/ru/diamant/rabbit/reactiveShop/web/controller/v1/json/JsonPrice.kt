package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.diamant.rabbit.reactiveShop.domain.Currency

data class JsonPrice(
    @JsonProperty("amount")
    val amount: Long,

    @JsonProperty("currency")
    val currency: Currency,
)
