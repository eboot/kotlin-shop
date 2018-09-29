package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Item(val product: Product, val quantity: Int) {
    val subtotal: BigDecimal = product.price
            .multiply(quantity.toBigDecimal())

    init {
        require(quantity > 0) { "Quantity must be > 0" }
    }
}

data class Product(val name: String, val type: ProductType, private val _price: Double) {
    val price: BigDecimal = _price
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)

    init {
        require(name.isNotBlank()) { "Product name cannot be blank" }
        require(_price > 0.0) { "Produce price must be > 0" }
    }
}

enum class ProductType {
    PHYSICAL,
    PHYSICAL_TAX_FREE,
    DIGITAL,
    SUBSCRIPTION
}
