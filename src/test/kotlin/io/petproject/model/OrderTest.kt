package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OrderTest {

    private lateinit var account: Account
    private lateinit var paymentMethod: PaymentMethod

    private lateinit var physicalItems: List<Item>
    private lateinit var physicalTaxFreeItems: List<Item>
    private lateinit var digitalItems: List<Item>
    private lateinit var subscriptions: List<Item>
    private lateinit var mixedItems: List<Item>


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
        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)
        val netflix = Product("Netflix Familiar Plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("Spotify Premium", Category.SUBSCRIPTION, 14.90)
        val amazon = Product("Amazon Prime", Category.SUBSCRIPTION, 12.90)

        physicalItems = listOf(
                Item(console, 1),
                Item(chair, 2)
        )
        physicalTaxFreeItems = listOf(
                Item(book, 2),
                Item(anotherBook, 1)
        )
        digitalItems = listOf(
                Item(musicDigitalAlbum, 1),
                Item(videoGameDigitalCopy, 4)
        )
        subscriptions = listOf(
                Item(netflix, 1),
                Item(spotify, 1),
                Item(amazon, 1)
        )
        mixedItems = listOf(physicalItems, digitalItems).flatten()
    }


    @Test
    fun `when placing a PhysicalOrder, there must be at least one item in the list`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            val order = PhysicalOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }


    @Test
    fun `when placing a PhysicalOrder, a paymentMethod must be informed`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            val order = PhysicalOrder(physicalItems, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

    @Test
    fun `when placing a Physical Order, a shippingAddress must be informed`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            val order = PhysicalOrder(physicalItems, account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("Shipping Address must be informed for Orders with physical delivery")
    }

    @Test
    fun `when placing a Physical Order with different Physical and Physical_Books, there should be different shipments`() {
        val physicalItems = listOf(physicalItems, physicalTaxFreeItems).flatten()
        val order = PhysicalOrder(physicalItems, account, paymentMethod)
                .shippingAddress(account.address)
        order.place()
        assertThat(order.shipments?.size).isEqualTo(2)
    }

    @Test
    fun `when placing a Physical Order with physical_books, its package must contain notes informing it's free of taxes`() {
        val physicalItems = listOf(physicalItems, physicalTaxFreeItems).flatten()
        val order = PhysicalOrder(physicalItems, account, paymentMethod)
                .shippingAddress(account.address)
        order.place()

        val parcel: Package? = order.shipments
                ?.asSequence()
                ?.find { s -> s.shippingLabel == ShippingLabel.TAX_FREE }

        assertThat(parcel?.shippingLabel?.label)
                .isEqualTo("Isento de impostos conforme disposto na Constituição Art. 150, VI, d.")
    }

    @Test
    fun `when placing a DigitalOrder, there must be at least one item in the list`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            val order = DigitalOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }

    @Test
    fun `when placing a DigitalOrder, a paymentMethod must be informed`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            val order = DigitalOrder(digitalItems, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }

    @Test
    fun `when placing a MembershipOrder, there must be at least one item in the list`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            val order = MembershipOrder(ArrayList(), account, paymentMethod)
            order.place()
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }

    @Test
    fun `when placing a MembershipOrder, a paymentMethod must be informed`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            val order = MembershipOrder(subscriptions, account, null)
            order.place()
        }
        assertThat(ex.message).isEqualTo("A Payment method must be informed to place the Order")
    }
}