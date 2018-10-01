package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Package(val items: List<Item>, val shippingAddress: Address, val shippingLabel: ShippingLabel) {

    constructor(items: List<Item>, shippingAddress: Address) : this(items, shippingAddress, ShippingLabel.DEFAULT)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to package" }
    }

    fun getShippingCosts(): BigDecimal {
        //TODO: Implement service to calc Shipping Costs for this Parcel
        return BigDecimal.TEN
                .setScale(2, RoundingMode.HALF_UP)
    }

}

enum class ShippingLabel(val label: String = "") {
    TAX_FREE("Isento de impostos conforme disposto na Constituição Art. 150, VI, d."),
    DEFAULT
}
