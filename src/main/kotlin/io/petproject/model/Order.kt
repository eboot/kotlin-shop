package io.petproject.model

class Order(val items: List<Item>, val account: Account, val payment: Payment) {

    lateinit var billingAddress: Address
    lateinit var shippingAddress: Address

    init {
        val productTypes = items.asSequence().map { item -> item.product.type }.toHashSet()
        check(productTypes.size == 1) { "Items must belong to the same Product Type" }
    }

}