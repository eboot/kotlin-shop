package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

interface Order {

    val items: List<Item>
    val account: Account
    var paymentMethod: PaymentMethod?
    val additional: Map<String, BigDecimal>
    var invoice: Invoice?
    var status: OrderStatus

    fun place() {
        require(items.isNotEmpty()) { "There must be at least one item to place the Order" }
        checkNotNull(paymentMethod) { "A Payment method must be informed to place the Order" }
    }

    fun pay(): Invoice {
        check((status.id < OrderStatus.PENDING.id).not()) { "Order must be placed before it can be payed" }
        check((status.id >= OrderStatus.UNSHIPPED.id).not()) { "Order Payment has been processed already" }
        return Invoice(this)
    }

    fun fulfill() {
        check((status.id < OrderStatus.UNSHIPPED.id).not()) { "Order must be placed and payed before it can be fulfilled" }
        check((status.id >= OrderStatus.SHIPPED.id).not()) { "Order Fulfillment has been processed already" }
    }

    fun complete() {
        check((status.id < OrderStatus.SHIPPED.id).not()) { "Order must have been shipped/sent and confirmed, before it can be completed" }
        check((status.id >= OrderStatus.DELIVERED.id).not()) { "Order has been delivered already" }
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
    override var invoice: Invoice? = null

    var shippingAddress: Address? = null
    var shipments: List<Package>? = null

    init {
        require(items.find { i -> i.group != ItemType.PHYSICAL } == null) {
            "A Physical Order may only contain Physical items"
        }
    }

    override
    fun place() {
        super.place()
        checkNotNull(shippingAddress) { "Shipping Address must be informed for Orders with physical delivery" }
        this.shipments = setupPackages()
        this.additional["Shipping and Handling"] = shipments?.asSequence()
                ?.map(Package::getShippingCosts)
                ?.reduce(BigDecimal::add)
                ?: BigDecimal.ZERO
                .setScale(2, RoundingMode.UNNECESSARY)
        this.status = OrderStatus.PENDING
    }

    override
    fun pay(): Invoice {
        // TODO: Process Payment
        val invoice = super.pay()
        this.status = OrderStatus.UNSHIPPED
        return invoice
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
        super.complete()
        // TODO: Track the Packages until all packages are delivered
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
    override var invoice: Invoice? = null

    init {
        require(items.find { i -> i.group != ItemType.DIGITAL } == null) {
            "A Digital Order may only contain Digital items"
        }
    }

    override
    fun place() {
        super.place()
        this.additional["Voucher"] = BigDecimal(-10).setScale(2, RoundingMode.UNNECESSARY)
        this.status = OrderStatus.PENDING
    }

    override
    fun pay(): Invoice {
        // TODO: Process Payment
        val invoice = super.pay()
        this.status = OrderStatus.UNSENT
        return invoice
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
        // TODO:: Track when the the Buyer clicks on the emailed link to redeem the item
        super.complete()
        this.status = OrderStatus.REEDEEMED
    }

}

class MembershipOrder(override val items: List<Item>,
                      override val account: Account,
                      override var paymentMethod: PaymentMethod?) : Order {

    override val additional: HashMap<String, BigDecimal> = HashMap()
    override var status: OrderStatus = OrderStatus.UNKNOWN
    override var invoice: Invoice? = null

    init {
        require(items.find { i -> i.group != ItemType.MEMBERSHIP } == null) {
            "A Subscription Order may only contain a Subscription Item"
        }
    }

    constructor(item: Item, account: Account, paymentMethod: PaymentMethod?) : this(listOf(item), account, paymentMethod)

    override
    fun place() {
        checkNotNull(paymentMethod) { "A Payment method must be informed to place the Order" }
        check((items.size == 1)) { "There must be only 1 subscription per Membership Order" }
        this.status = OrderStatus.PENDING
    }

    override
    fun pay(): Invoice {
        // TODO: Process Payment
        val invoice = super.pay()
        this.status = OrderStatus.PENDING_ACTIVATION
        return invoice
    }

    override
    fun fulfill() {
        super.fulfill()
        // TODO: Activate the Subscription Service
        this.status = OrderStatus.ACTIVATED
    }

    override
    fun total(): BigDecimal {
        return super.subtotal()
    }

}

enum class OrderStatus(val id: Int = 0) {
    UNKNOWN,
    PENDING(100),
    UNSHIPPED(200),
    UNSENT(200),
    PENDING_ACTIVATION(200),
    SHIPPED(300),
    SENT(300),
    DELIVERED(400),
    REEDEEMED(400),
    ACTIVATED(400)
}
