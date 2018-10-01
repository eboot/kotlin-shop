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
    fun `when placing a MembershipOrder, there must be at least one item in the list`() {
        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            val order = MembershipOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }

    @Test
    fun `when placing a MembershipOrder, a paymentMethod must be informed`() {
        val ex = Assertions.assertThrows(IllegalStateException::class.java) {
            val order = MembershipOrder(subscriptions, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

}