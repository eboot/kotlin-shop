package io.petproject.model

import java.lang.ClassCastException
import java.math.BigDecimal

data class Item(val product: Product, var quantity: Int) {

    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    val group: ItemType by lazy {
        when (product.category) {
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

    override fun equals(other: Any?): Boolean {
        return try {
            val that = other as Item
            (this.product == that.product)
        } catch (ex: ClassCastException) {
            false
        }
    }
}

enum class ItemType {
    PHYSICAL,
    DIGITAL,
    MEMBERSHIP
}
