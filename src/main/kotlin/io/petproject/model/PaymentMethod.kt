package io.petproject.model

interface PaymentMethod {

    val billingAddress: Address

    fun charge(): Boolean

}

data class Invoice(val items: List<Item>, val billingAddress: Address, val shippingAddress: Address?) {

    constructor(items: List<Item>, billingAddress: Address) : this(items, billingAddress, null)

}
