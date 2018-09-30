package io.petproject.model

import java.math.BigDecimal

data class Item(val product: Product, val quantity: Int) {
    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    val group: ItemGroup by lazy {
        when(product.category) {
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
