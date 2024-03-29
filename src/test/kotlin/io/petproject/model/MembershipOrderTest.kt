package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MembershipOrderTest {

    private lateinit var account: Account
    private lateinit var paymentMethod: PaymentMethod
    private lateinit var subscriptions: List<Item>


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

        val netflix = Product("Netflix Familiar Plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("Spotify Premium", Category.SUBSCRIPTION, 14.90)
        val amazon = Product("Amazon Prime", Category.SUBSCRIPTION, 12.90)

        subscriptions = listOf(
                Item(netflix, 1),
                Item(spotify, 1),
                Item(amazon, 1)
        )
    }

    @Test
    fun `when creating a Membership Order, there must be only a Membership item the list`() {
        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            MembershipOrder(
                    listOf(Item(Product("physical product", Category.PHYSICAL, 1.99), 1)),
                    account, paymentMethod
            )
        }
        assertThat(ex.message).isEqualTo("A Subscription Order may only contain a Subscription Item")
    }

    @Test
    fun `when placing a MembershipOrder, there must be exactly one item in the list`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = MembershipOrder(subscriptions, account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be only 1 subscription per Membership Order")
    }

    @Test
    fun `when placing a MembershipOrder, a paymentMethod must be informed`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = MembershipOrder(subscriptions[0], account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

    @Test
    fun `when placing a Membership Order, subtotal should be equalTo the Item price`() {
        val order = buildOrder()
        order.place()
        assertThat(order.subtotal().toPlainString()).isEqualTo("29.90")
    }

    @Test
    fun `when placing a Membership Order, total should be equalTo the Subtotal`() {
        val order = buildOrder()
        order.place()
        assertThat(order.total().toPlainString()).isEqualTo("29.90")
    }

    @Test
    fun `when paying for Membership Order, throw IllegalStateEx if Status is not PENDING`() {
        val order = buildOrder()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.pay()
        }
        assertThat(ex.message).isEqualTo("Order must be placed before it can be payed")
    }

    @Test
    fun `when paying for Membership Order, Status should be updated to PENDING_ACTIVATION once pay is successful`() {
        val order = buildOrder()
        order.place()
        order.pay()
        assertThat(order.status).isEqualTo(OrderStatus.PENDING_ACTIVATION)
    }

    @Test
    fun `when paying for Membership Order that was already payed, throw IllegalArgEx`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.pay()
        }
        assertThat(ex.message).isEqualTo("Order Payment has been processed already")
    }

    @Test
    fun `when fulfilling a Membership Order, throw IllegalStateEx if Status is not PAYMENT_COMPLETE`() {
        val order = buildOrder()
        order.place()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.fulfill()
        }
        assertThat(ex.message).isEqualTo("Order must be placed and payed before it can be fulfilled")
    }

    @Test
    fun `when fulfilling a Membership Order, Status should be updated to ACTIVATED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        assertThat(order.status).isEqualTo(OrderStatus.ACTIVATED)
    }

    private fun buildOrder(): Order {
        return MembershipOrder(subscriptions[0], account, null)
                .paymentMethod(paymentMethod)
    }

}