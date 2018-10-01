package io.petproject.model

import java.math.BigDecimal

interface Order {

    val items: List<Item>
    val account: Account
    var paymentMethod: PaymentMethod?
    val additional: Map<String, BigDecimal>
    var status: OrderStatus

    fun place() {
        require(items.isNotEmpty()) { "There must be at least one item to place the Order" }
        checkNotNull(paymentMethod) { "A Payment method must be informed to place the Order" }
    }

    fun paymentMethod(paymentMethod: PaymentMethod): Order {
        this.paymentMethod = paymentMethod
        return this
    }

    fun subtotal(): BigDecimal {
        return items.asSequence()
                .map(Item::subtotal)
                .reduce(BigDecimal::add)
    }

    fun total(): BigDecimal {
        return subtotal().add(additional.values.reduce(BigDecimal::add))
    }

}

class PhysicalOrder(override val items: List<Item>,
                    override val account: Account,
                    override var paymentMethod: PaymentMethod?) : Order {

    override val additional: HashMap<String, BigDecimal> = HashMap()
    override var status: OrderStatus = OrderStatus.PENDING
    var shippingAddress: Address? = null
    var shipments: List<Package>? = null

    override
    fun place() {
        super.place()
        checkNotNull(shippingAddress) { "Shipping Address must be informed for Orders with physical delivery" }

        this.shipments = setupPackages()
        additional["Shipping & Handling"] = shipments?.asSequence()
                ?.map(Package::getShippingCosts)
                ?.reduce(BigDecimal::add)
                ?: BigDecimal.ZERO
    }

    fun shippingAddress(address: Address): PhysicalOrder {
        this.shippingAddress = address
        return this
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

class DigitalOrder(override val items: List<Item>,
                   override val account: Account,
                   override var paymentMethod: PaymentMethod?) : Order {

    override val additional: HashMap<String, BigDecimal> = HashMap()
    override var status: OrderStatus = OrderStatus.PENDING

    override fun total(): BigDecimal {
        val total = super.total()
        val subtotal = super.total()
        val discounts = BigDecimal.ZERO // TODO("Compute Digital Media discounts")
        return subtotal.subtract(discounts)
    }
}

class MembershipOrder(override val items: List<Item>,
                      override val account: Account,
                      override var paymentMethod: PaymentMethod?) : Order {

    override val additional: HashMap<String, BigDecimal> = HashMap()
    override var status: OrderStatus = OrderStatus.PENDING

    constructor(item: Item, account: Account, paymentMethod: PaymentMethod?) : this(listOf(item), account, paymentMethod)
}

enum class OrderStatus {
    PENDING,
    UNSHIPPED,
    PARTIALLY_SHIPPED,
    SHIPPED,
    DELIVERED
}
