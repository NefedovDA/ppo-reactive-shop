package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("shop.users_configs")
data class UserConfig(
    @Id
    @Column("user_config_id")
    val id: Long?,

    @Column("user_id")
    val userId: Long,

    @Column("default_currency")
    val defaultCurrencyName: String
)

val UserConfig.defaultCurrency: Currency
    get() = Currency.valueOf(defaultCurrencyName)
