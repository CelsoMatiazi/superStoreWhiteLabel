package com.crsm.whitelabelstore.domain.usecase

import com.crsm.whitelabelstore.domain.model.Product

interface GetProductsUseCase {
    suspend operator fun invoke(): List<Product>
}