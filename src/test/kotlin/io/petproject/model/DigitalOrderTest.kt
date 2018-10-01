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

}