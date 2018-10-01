package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DigitalOrderTest {

    private lateinit var account: Account
    private lateinit var paymentMethod: PaymentMethod
    private lateinit var digitalItems: List<Item>


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

        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)

        digitalItems = listOf(
                Item(musicDigitalAlbum, 1),
                Item(videoGameDigitalCopy, 4)
        )
    }

    @Test
    fun `when placing a DigitalOrder, there must be at least one item in the list`() {
        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            val order = DigitalOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }

    @Test
    fun `when placing a DigitalOrder, a paymentMethod must be informed`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = DigitalOrder(digitalItems, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

    @Test
    fun `when placing a Digital Order, subtotal should compute overall sum of all Item prices`() {
        val order = buildOrder()
        order.place()
        assertThat(order.subtotal().toPlainString()).isEqualTo("3256.14")
    }

    @Test
    fun `when placing a Digital Order, total should compute subtotal plus discounts for Digital Items`() {
        val order = buildOrder()
        order.place()
        assertThat(order.total().toPlainString()).isEqualTo("3276.14")
    }

    @Test
    fun `when paying for Digital Order, throw IllegalStateEx if Status is not PENDING`() {
        val order = buildOrder()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.pay()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when paying for Digital Order, Status should be updated to UNSHIPPED once pay is successful`() {
        val order = buildOrder()
        order.place()
        order.pay()
        assertThat(order.status).isEqualTo(OrderStatus.UNSENT)
    }

    @Test
    fun `when fulfilling a Digital Order, throw IllegalStateEx if Status is not PAYMENT_COMPLETE`() {
        val order = buildOrder()
        order.place()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.fulfill()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when fulfilling a Digital Order, Status should be updated to SENT`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        assertThat(order.status).isEqualTo(OrderStatus.SENT)
    }

    @Test
    fun `when completing a Digital Order, throw IllegalStateEx if Status is not SHIPPED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            order.complete()
        }
        assertThat(ex.message).isEqualTo("")
    }

    @Test
    fun `when completing a Digital Order, Status should be updated to REDEEMED`() {
        val order = buildOrder()
        order.place()
        order.pay()
        order.fulfill()
        order.complete()
        assertThat(order.status).isEqualTo(OrderStatus.DELIVERED)
    }


    private fun buildOrder(): Order {
        return DigitalOrder(digitalItems, account, null)
                .paymentMethod(paymentMethod)
    }

}