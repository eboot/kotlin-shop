package io.petproject.model

import java.math.BigDecimal

data class Item(val product: Product, val quantity: Int) {
    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    val group: ItemGroup by lazy {
        when(product.category) {
            Category.DIGITAL_COPY_MOVIES_TV -> ItemGroup.DIGITAL
            Category.DIGITAL_MUSIC -> ItemGroup.DIGITAL
            Category.DIGITAL_VIDEO_GAMES -> ItemGroup.DIGITAL
            Category.DIGITAL_SOFTWARE -> ItemGroup.DIGITAL
            Category.SUBSCRIPTION -> ItemGroup.MEMBERSHIP
            else -> ItemGroup.PHYSICAL
        }
    }

    init {
        require(quantity > 0) { "Quantity must be > 0" }
    }
}

enum class ItemGroup {
    PHYSICAL,
    DIGITAL,
    MEMBERSHIP
}
