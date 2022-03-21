package ru.diamant.rabbit.reactiveShop.web.controller.v1.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.diamant.rabbit.reactiveShop.domain.Product
import ru.diamant.rabbit.reactiveShop.domain.price

data class JsonProductInfo(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("price")
    val price: JsonPrice,

    @JsonProperty("owner_id")
    val ownerId: Long,
) {
    companion object {
        operator fun invoke(product: Product): JsonProductInfo =
            JsonProductInfo(
                id = requireNotNull(product.id) { "Non DB product?" },
                title = product.title,
                description = product.description,
                price = JsonPrice(product.price.amount, product.price.currency),
                ownerId = product.ownerId
            )
    }
}
