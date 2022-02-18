package ru.diamant.rabbit.reactiveShop.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import ru.diamant.rabbit.reactiveShop.domain.Product

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, Long>