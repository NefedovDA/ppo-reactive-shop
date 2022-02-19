package ru.diamant.rabbit.reactiveShop.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

object Authority {
    const val USER = "USER"
    const val ADMIN = "ADMIN"
    const val OWNER = "OWNER"
}

@Suppress("unused") // processed by DB
enum class Role(vararg authorityNames: String) {
    USER(Authority.USER),
    ADMIN(Authority.USER, Authority.ADMIN),
    OWNER(Authority.USER, Authority.ADMIN, Authority.OWNER);

    val authorities: Set<GrantedAuthority> =
        authorityNames
            .map { SimpleGrantedAuthority(it) }
            .toSet()
}
