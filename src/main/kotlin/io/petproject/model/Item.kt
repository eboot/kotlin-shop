package io.petproject.model

import java.math.BigDecimal

data class Item(val product: Product, val quantity: Int) {

    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    val group: ItemType by lazy {
        when(product.category) {
            Category.DIGITAL_COPY_MOVIES_TV -> ItemType.DIGITAL
            Category.DIGITAL_MUSIC -> ItemType.DIGITAL
            Category.DIGITAL_VIDEO_GAMES -> ItemType.DIGITAL
            Category.DIGITAL_SOFTWARE -> ItemType.DIGITAL
            Category.SUBSCRIPTION -> ItemType.MEMBERSHIP
            else -> ItemType.PHYSICAL
        }
    }

    init {
        require(quantity > 0) { "Quantity must be > 0" }
    }
}

enum class ItemType {
    PHYSICAL,
    DIGITAL,
    MEMBERSHIP
}
