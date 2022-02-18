package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table("users")
data class User(
    @Id
    @Column("user_id")
    val id: Long,

    @Column("full_name")
    val fullName: String,

    @Column("selected_currency")
    val selectedCurrency: Currency,

    @Column("email")
    val email: String,

    @Column("password_hash")
    val passwordHash: String,

    @Column("user_role")
    val role: Role,
)

