package io.petproject.model

data class Order(val items: List<Item>, val account: Account) {

    lateinit var payment: Payment
    lateinit var billingAddress: Address
    var shippingAddress: Address? = null
    var shipments: List<Package>? = null
    val type: Type

    constructor(item: Item, account: Account): this(listOf(item), account)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to place the Order" }
        check(items.asSequence()
                .map { item -> item.group }
                .toHashSet().size == 1) { "Items must belong to the same Item Group" }
        type = items[0].group
    }

    fun place() {
        if (type == Type.PHYSICAL) {
            checkNotNull(shippingAddress) { "Shipping Address must be informed for Orders with physical delivery" }
        }
    }

    fun shippingAddress(address: Address): Order {
        this.shippingAddress = address
        return this
    }

}

data class Package(val items: List<Item>, val shippingLabel: Address) {

    init {
        require(items.isNotEmpty()) { "There must be at least one item to package" }
    }
}
