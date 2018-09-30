package io.petproject.model

data class Order(val items: List<Item>, val account: Account) {

    lateinit var billingAddress: Address
    lateinit var shippingAddress: Address
    lateinit var payment: Payment

    constructor(item: Item, account: Account): this(listOf(item), account)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to place the Order" }
        check(items.asSequence()
                .map { item -> item.group }
                .toHashSet().size == 1) { "Items must belong to the same Item Group" }
    }
}