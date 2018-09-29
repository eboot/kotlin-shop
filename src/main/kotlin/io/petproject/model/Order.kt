package io.petproject.model

class Order(val items: List<Item>, val account: Account) {

    lateinit var billingAddress: Address
    lateinit var shippingAddress: Address
    lateinit var payment: Payment

    init {
        val productTypes = items.asSequence().map { item -> item.product.type }.toHashSet()
        check(productTypes.size == 1) { "Items must belong to the same Product Type" }
    }

}