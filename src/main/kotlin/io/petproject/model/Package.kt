package io.petproject.model

data class Package(val items: List<Item>, val shippingAddress: Address, val shippingLabel: ShippingLabel) {

    constructor(items: List<Item>, shippingAddress: Address) : this(items, shippingAddress, ShippingLabel.DEFAULT)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to package" }
    }
}

enum class ShippingLabel(val label: String = "") {
    TAX_FREE("Isento de impostos conforme disposto na Constituição Art. 150, VI, d."),
    DEFAULT
}
