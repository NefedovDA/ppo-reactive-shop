package ru.diamant.rabbit.reactiveShop.security

import ru.diamant.rabbit.reactiveShop.domain.User
import ru.diamant.rabbit.reactiveShop.domain.role

data class UserDetails(
    val user: User
) : SecurityUser(
    user.login,
    user.password,
    user.role.authorities
)