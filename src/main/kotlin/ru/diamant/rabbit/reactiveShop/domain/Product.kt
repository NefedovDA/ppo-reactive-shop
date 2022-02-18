package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("products")
data class Product(
    @Id
    @Column("product_id")
    val id: Long,

    @Column("title")
    val title: String,

    @Column("description")
    val description: String,

    @Column("amount")
    val amount: String,

    @Column("price")
    val price: String
)