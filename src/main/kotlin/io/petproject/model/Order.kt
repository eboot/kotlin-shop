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

    fun pay() {
        check((status.id < OrderStatus.PENDING.id).not()) { "Order must be placed before it can be payed" }
        check((status.id >= OrderStatus.PAYMENT_COMPLETE.id).not()) { "Order Payment has been processed already" }
        this.status = OrderStatus.PAYMENT_COMPLETE
    }

    fun fulfill() {
        check((status.id < OrderStatus.PAYMENT_COMPLETE.id).not()) { "Order must be placed and payed before it can be fulfilled" }
        check((status.id >= OrderStatus.SENT.id).not()) { "Order Fulfillment has been processed already"}
    }

    fun complete() {
        check((status.id < OrderStatus.SHIPPED.id).not()) { "Order must have confirmed shipping/sent before it can be complete" }
        check((status.id > OrderStatus.DELIVERED.id).not()) { "Order has been delivered already" }
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
    override var status: OrderStatus = OrderStatus.UNKNOWN
    var shippingAddress: Address? = null
    var shipments: List<Package>? = null

    override
    fun place() {
        super.place()
        checkNotNull(shippingAddress) { "Shipping Address must be informed for Orders with physical delivery" }
        this.shipments = setupPackages()
        this.additional["Shipping & Handling"] = shipments?.asSequence()
                ?.map(Package::getShippingCosts)
                ?.reduce(BigDecimal::add)
                ?: BigDecimal.ZERO
        this.status = OrderStatus.PENDING
    }

    override
    fun fulfill() {
        super.fulfill()
        // TODO: Notify Buyer via email
        // TODO: Notify Seller about the Order to initiate the Processing & Shipping
        this.status = OrderStatus.SHIPPED
    }

    override
    fun complete() {
        this.status = OrderStatus.DELIVERED
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
    override var status: OrderStatus = OrderStatus.UNKNOWN

    override
    fun place() {
        super.place()
        this.additional["Voucher"] = BigDecimal(-10)
        this.status = OrderStatus.PENDING
    }

    override
    fun fulfill() {
        super.fulfill()
        // TODO: Notify Buyer via email
        // TODO: Prepare Download Link and send it to the buyer
        this.status = OrderStatus.SENT
    }

    override
    fun complete() {
        this.status = OrderStatus.REEDEEMED
    }

}

class MembershipOrder(override val items: List<Item>,
                      override val account: Account,
                      override var paymentMethod: PaymentMethod?) : Order {

    override val additional: HashMap<String, BigDecimal> = HashMap()
    override var status: OrderStatus = OrderStatus.UNKNOWN

    constructor(item: Item, account: Account, paymentMethod: PaymentMethod?) : this(listOf(item), account, paymentMethod)

    override
    fun place() {
        super.place()
        this.status = OrderStatus.PENDING
    }

    override
    fun fulfill() {
        super.fulfill()
        // TODO: Notify Buyer via email
        // TODO: Activate the Subscription
        this.status = OrderStatus.ACTIVATED
    }

    override
    fun complete() {

    }

}

enum class OrderStatus(val id: Int = 0) {
    UNKNOWN,
    PENDING(100),
    PAYMENT_COMPLETE(150),
    UNSHIPPED(200),
    UNSENT(200),
    PENDING_ACTIVATION(200),
    SHIPPED(300),
    SENT(300),
    DELIVERED(400),
    REEDEEMED(400),
    ACTIVATED(400)
}

fun main(args: Array<String>) {
    check(!(0 < 1)) { "prints if it is true " }
}