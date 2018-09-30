package io.petproject.model

import java.math.BigDecimal

data class Item(val product: Product, val quantity: Int) {

    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    val group: Type by lazy {
        when(product.category) {
            Category.DIGITAL_COPY_MOVIES_TV -> Type.DIGITAL
            Category.DIGITAL_MUSIC -> Type.DIGITAL
            Category.DIGITAL_VIDEO_GAMES -> Type.DIGITAL
            Category.DIGITAL_SOFTWARE -> Type.DIGITAL
            Category.SUBSCRIPTION -> Type.MEMBERSHIP
            else -> Type.PHYSICAL
        }
    }

    init {
        require(quantity > 0) { "Quantity must be > 0" }
    }
}
