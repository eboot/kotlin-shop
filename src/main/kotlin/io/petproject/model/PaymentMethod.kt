package io.petproject.model

import java.math.BigDecimal

interface PaymentMethod {

    val billingAddress: Address

    fun charge(): Boolean

}

data class Invoice(val items: List<Item>,
                   val subtotal: BigDecimal,
                   val other: Map<String, BigDecimal>,
                   val total: BigDecimal,
                   val billingAddress: Address, val shippingAddress: Address?) {

    constructor(items: List<Item>,
                subtotal: BigDecimal, other: Map<String, BigDecimal>, total: BigDecimal,
                billingAddress: Address) :
            this(items, subtotal, other, total, billingAddress, null)

}
