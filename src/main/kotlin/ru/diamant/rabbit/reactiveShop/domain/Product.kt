package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("shop.products")
data class Product(
    @Id
    @Column("product_id")
    val id: Long?,

    @Column("title")
    val title: String,

    @Column("description")
    val description: String,

    @Column("price_amount")
    val price_amount: Long,

    @Column("price_currency")
    val price_currency: String,

    @Column("user_id")
    val ownerId: Long
)

val Product.price: Price
    get() = Price(price_amount, Currency.valueOf(price_currency))
