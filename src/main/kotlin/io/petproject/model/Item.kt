package io.petproject.model

data class Item(val product: Product, val quantity: Int) {
    val subtotal = product.price * quantity
}

data class Product(val name: String, val type: ProductType, val price: Double)

enum class ProductType {
    PHYSICAL,
    PHYSICAL_TAX_FREE,
    DIGITAL,
    SUBSCRIPTION
}
