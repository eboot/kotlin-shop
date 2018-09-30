package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

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
    DIGITAL_MEDIA,
    MEMBERSHIP
}
