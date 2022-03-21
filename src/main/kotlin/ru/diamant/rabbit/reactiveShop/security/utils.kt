package ru.diamant.rabbit.reactiveShop.security

import org.springframework.security.core.Authentication
import ru.diamant.rabbit.reactiveShop.domain.User

val Authentication.userDetails: UserDetails
    get() = checkNotNull(principal as? UserDetails) { "Bad Authentication" }

val Authentication.user: User
    get() = userDetails.user
