package io.petproject.model

class Order(val items: List<Item>, val account: Account) {

    private val type: Type
    var shippingAddress: Address? = null
    var shipments: List<Package>? = null
    var paymentMethod: PaymentMethod? = null
    var status: ShippingStatus? = null

    constructor(item: Item, account: Account) : this(listOf(item), account)

    init {
        require(items.isNotEmpty()) { "There must be at least one item to place the Order" }
        check(items.asSequence()
                .map { item -> item.group }
                .toHashSet().size == 1) { "Items must belong to the same Item Group" }
        type = items[0].group
    }

    fun shippingAddress(address: Address): Order {
        this.shippingAddress = address
        return this
    }

    fun paymentMethod(paymentMethod: PaymentMethod): Order {
        this.paymentMethod = paymentMethod
        return this
    }

    fun place(): Order {
        if (type == Type.PHYSICAL) {
            checkNotNull(shippingAddress) { "Shipping Address must be informed for Orders with physical delivery" }
            this.shipments = setupPackages()
        }
        checkNotNull(paymentMethod) { "A Payment Method must be informed to place the order" }
        this.status = ShippingStatus.PENDING
        return this //TODO("Persist Order and get the Order ID")
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

}

enum class ShippingStatus {
    PENDING,
    UNSHIPPED,
    PARTIALLY_SHIPPED,
    SHIPPED,
    DELIVERED
}


