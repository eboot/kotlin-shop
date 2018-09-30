package io.petproject.model

import java.math.BigDecimal

data class Item(val product: Product, val quantity: Int) {
    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    init {
        require(quantity > 0) { "Quantity must be > 0" }
    }
}

