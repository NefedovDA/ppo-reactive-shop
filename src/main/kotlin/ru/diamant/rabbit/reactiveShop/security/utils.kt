package ru.diamant.rabbit.reactiveShop.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import ru.diamant.rabbit.reactiveShop.domain.Authority
import ru.diamant.rabbit.reactiveShop.domain.User

val Authentication.userDetails: UserDetails
    get() = checkNotNull(principal as? UserDetails) { "Bad Authentication" }

val Authentication.user: User
    get() = userDetails.user

val Authentication.isAdmin: Boolean
    get() = SimpleGrantedAuthority(Authority.ADMIN) in authorities
