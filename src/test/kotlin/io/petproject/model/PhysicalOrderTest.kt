package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PhysicalOrderTest {

    private lateinit var account: Account
    private lateinit var paymentMethod: PaymentMethod
    private lateinit var physicalItems: List<Item>
    private lateinit var physicalTaxFreeItems: List<Item>


    @BeforeEach
    fun setup() {
        val address = Address.Builder()
                .country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build()

        account = Account("email@domain.suffix", address)
        paymentMethod = object : PaymentMethod {
            override val billingAddress: Address = address
            override fun charge(): Boolean = true
        }

        val console = Product("PS4 Slim 1TB", Category.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", Category.PHYSICAL, 399.00)
        val book = Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00)

        physicalItems = listOf(
                Item(console, 1),
                Item(chair, 2)
        )
        physicalTaxFreeItems = listOf(
                Item(book, 2),
                Item(anotherBook, 1)
        )
    }

    @Test
    fun `when placing a PhysicalOrder, there must be at least one item in the list`() {
        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            val order = PhysicalOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }

    @Test
    fun `when placing a PhysicalOrder, a paymentMethod must be informed`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = PhysicalOrder(physicalItems, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

    @Test
    fun `when placing a Physical Order, a shippingAddress must be informed`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = PhysicalOrder(physicalItems, account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("Shipping Address must be informed for Orders with physical delivery")
    }

    @Test
    fun `when placing a Physical Order with Physical and Physical_Books, there should be different shipments`() {
        val order = buildOrder() as PhysicalOrder
        order.place()
        assertThat(order.shipments?.size).isEqualTo(2)
    }

    @Test
    fun `when placing a Physical Order with physical_books, its package must contain notes informing it's free of taxes`() {
        val order = buildOrder() as PhysicalOrder
        order.place()

        val parcel: Package? = order.shipments
                ?.asSequence()
                ?.find { s -> s.shippingLabel == ShippingLabel.TAX_FREE }

        assertThat(parcel?.shippingLabel?.label)
                .isEqualTo("Isento de impostos conforme disposto na Constituição Art. 150, VI, d.")
    }

    @Test
    fun `when placing a Physical Order, subtotal should compute overall sum of all Item prices`() {
        val order = buildOrder()
        order.place()
        assertThat(order.subtotal().toPlainString()).isEqualTo("3256.14")
    }

    @Test
    fun `when placing a Physical Order, total should compute subtotal plus shippingCosts`() {
        val order = buildOrder()
        order.place()
        assertThat(order.total().toPlainString()).isEqualTo("3276.14")
    }

    @Test
    fun `when paying for Physical Order, throw IllegalStateEx if Status is not PENDING`() {
        val order = buildOrder()
        val ex = assertThrows(IllegalStateException::class.java) {
            order.pay()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when paying for Physical Order, Status should be updated to UNSHIPPED once the payment is done`() {
        val order = buildOrder()
        order.place()
        order.pay()
        assertThat(order.status).isEqualTo(OrderStatus.UNSHIPPED)
    }

    @Test
    fun `when fulfilling a Physical Order, throw IllegalStateEx if Status is not PAYMENT_COMPLETE`() {
        val order = buildOrder()
        order.place()
        val ex = assertThrows(IllegalStateException::class.java) {
            order.fulfill()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when fulfilling a Physical Order, Status should be updated to SHIPPED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        assertThat(order.status).isEqualTo(OrderStatus.SHIPPED)
    }

    @Test
    fun `when completing a Physical Order, throw IllegalStateEx if Status is not SHIPPED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        val ex = assertThrows(IllegalStateException::class.java) {
            order.complete()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when completing a Physical Order, Status should be updated to DELIVERED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        order.complete()
        assertThat(order.status).isEqualTo(OrderStatus.DELIVERED)
    }

    private fun buildOrder(): Order {
        val physicalItems = listOf(physicalItems, physicalTaxFreeItems).flatten()
        return PhysicalOrder(physicalItems, account, null)
                .shippingAddress(account.address)
                .paymentMethod(paymentMethod)
    }

}