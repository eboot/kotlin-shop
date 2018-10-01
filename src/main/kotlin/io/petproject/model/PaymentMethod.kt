package io.petproject.model

interface PaymentMethod {

    val billingAddress: Address

    fun charge(): Boolean

}

data class Invoice(private val order: Order) {
    val items = order.items
    val subtotal = order.subtotal()
    val otherCosts = order.additional
    val total = order.total()
    val billingAddress = order.paymentMethod?.billingAddress
    val shipments = (order as? PhysicalOrder)?.shippingAddress
}
