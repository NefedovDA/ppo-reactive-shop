package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.diamant.rabbit.reactiveShop.domain.Currency

data class JsonUserConfigInfo(
    @JsonProperty("default_currency")
    val defaultCurrency: Currency
)