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
            shipments = setupPackages()
        }
    }

    private fun setupPackages(): List<Package> {
        return items.asSequence()
                .groupBy { item ->
                    when (item.product.category) {
                        Category.PHYSICAL_BOOK -> ShippingLabel.TAX_FREE
                        else -> ShippingLabel.DEFAULT
                    }
                }.map { (label, items) ->
                    Package(items, account.address, label)
                }.toList()
    }

    fun shippingAddress(address: Address): Order {
        this.shippingAddress = address
        return this
    }

}

data class Package(val items: List<Item>, val shippingAddress: Address, val label: ShippingLabel) {

    constructor(items: List<Item>, shippingAddress: Address): this(items, shippingAddress, ShippingLabel.DEFAULT)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to package" }
    }
}

enum class ShippingLabel(val label: String = "") {
    TAX_FREE("Isento de impostos conforme disposto na Constituição Art. 150, VI, d."),
    DEFAULT
}
