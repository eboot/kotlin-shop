package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Product(val name: String, val category: Category, private val _price: Double) {
    val price: BigDecimal = _price
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
    init {
        require(name.isNotBlank()) { "Product name cannot be blank" }
        require(_price > 0.0) { "Produce price must be > 0" }
    }

}

enum class Category {
    PHYSICAL,
    PHYSICAL_BOOK,
    DIGITAL_MUSIC,
    DIGITAL_COPY_MOVIES_TV,
    DIGITAL_VIDEO_GAMES,
    DIGITAL_SOFTWARE,
    SUBSCRIPTION
}
