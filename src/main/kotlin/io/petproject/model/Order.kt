package io.petproject.model

data class Order(val items: List<Item>, val account: Account) {

    lateinit var billingAddress: Address
    lateinit var shippingAddress: Address
    lateinit var payment: Payment

    init {
        val itemGroup = items.asSequence().map { item -> item.group }.toHashSet()
        check(itemGroup.size == 1) { "Items must belong to the same Item Group" }
    }
}