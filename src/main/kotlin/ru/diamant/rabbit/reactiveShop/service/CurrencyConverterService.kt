package ru.diamant.rabbit.reactiveShop.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Currency
import ru.diamant.rabbit.reactiveShop.web.client.CurrencyConverterClient
import ru.diamant.rabbit.reactiveShop.web.controller.v1.json.JsonPrice

@Service
class CurrencyConverterService(
    private val currencyConverterClient: CurrencyConverterClient
) {
    fun convert(price: JsonPrice, newCurrency: Currency): Mono<JsonPrice> {
        return currencyConverterClient.exchange(price, newCurrency)
    }
}
