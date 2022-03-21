package ru.diamant.rabbit.reactiveShop.web.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Currency
import ru.diamant.rabbit.reactiveShop.web.client.json.JsonExchangeResult
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonPrice

@Component
class CurrencyConverterClient(
    @Value("\${reactive.shop.currency.converter.api.url}")
    private val apiUrl: String,

    @Value("\${reactive.shop.currency.converter.api.key}")
    private val apiKey: String,
) {
    private val webClient = WebClient.create(apiUrl)

    fun exchange(price: JsonPrice, newCurrency: Currency): Mono<JsonPrice> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("api_key", apiKey)
                    .queryParam("from", price.currency.name)
                    .queryParam("to", newCurrency.name)
                    .queryParam("amount", price.amount)
                    .queryParam("format", "json")
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    response.bodyToMono(JsonExchangeResult::class.java)
                        .log()
                        .map { JsonPrice(it.rates.getValue(newCurrency.name).rate.toLong(), newCurrency) }
                        .onErrorResume { Mono.empty() }
                }
                else {
                    Mono.empty()
                }
            }
    }
}
