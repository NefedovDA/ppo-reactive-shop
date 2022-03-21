package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("authority.users")
data class User(
    @Id
    @Column("user_id")
    val id: Long?,

    @Column("login")
    val login: String,

    @Column("password")
    val password: String,

    @Column("role")
    val roleName: String,
)

val User.role: Role
    get() = Role.valueOf(roleName)
