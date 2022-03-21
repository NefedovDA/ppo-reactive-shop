package ru.diamant.rabbit.reactiveShop.domain

enum class Currency {
    RUB, USD, EUR;

    companion object {
        val DEFAULT: Currency = RUB
    }
}
