package ru.diamant.rabbit.reactiveShop.web.client.json

import com.fasterxml.jackson.annotation.JsonProperty

data class JsonExchangeResult(
    @JsonProperty("rates")
    val rates: Map<String, JsonRate>
) {
    data class JsonRate(
        @JsonProperty("rate_for_amount")
        val rate: Float
    )
}