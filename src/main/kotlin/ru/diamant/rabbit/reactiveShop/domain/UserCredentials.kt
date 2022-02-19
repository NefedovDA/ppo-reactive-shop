package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.security.core.userdetails.UserDetails
import ru.diamant.rabbit.reactiveShop.security.SecurityUser

interface UserCredentials {
    val email: String
    val password: String
    val role: Role
}

val UserCredentials.details: UserDetails
    get() = SecurityUser(
        email,
        password,
        role.authorities
    )
