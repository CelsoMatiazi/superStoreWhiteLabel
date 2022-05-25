package com.crsm.whitelabelstore.domain.usecase

import com.crsm.whitelabelstore.data.ProductRepository
import com.crsm.whitelabelstore.domain.model.Product
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : GetProductsUseCase {

    override suspend fun invoke(): List<Product> {
        return productRepository.getProducts()
    }
}