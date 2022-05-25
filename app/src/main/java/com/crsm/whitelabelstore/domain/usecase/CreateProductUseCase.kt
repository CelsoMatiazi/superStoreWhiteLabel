package com.crsm.whitelabelstore.domain.usecase

import android.net.Uri
import com.crsm.whitelabelstore.domain.model.Product

interface CreateProductUseCase {
    suspend operator fun invoke(description: String, price: Double, imageUri: Uri): Product
}