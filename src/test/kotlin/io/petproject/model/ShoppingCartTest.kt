package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ShoppingCartTest {

    private val shoppingCart = ShoppingCart()
    private lateinit var account: Account

    @BeforeEach
    fun `setup`() {
        val console = Product("PS4 Slim 1TB", Category.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", Category.PHYSICAL, 399.00)
        val netflix = Product("Nnetflix Familiar Plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("Spotify Premium", Category.SUBSCRIPTION, 14.90)
        val amazon = Product("Amazon Prime", Category.SUBSCRIPTION, 12.90)
        val book = Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00)
        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)

        shoppingCart.addItem(Item(console, 1))
                .addItem(Item(chair, 2))
                .addItem(Item(book, 2))
                .addItem(Item(anotherBook, 1))
                .addItem(Item(musicDigitalAlbum, 1))
                .addItem(Item(videoGameDigitalCopy, 4))
                .addItem(Item(netflix, 1))
                .addItem(Item(spotify, 1))
                .addItem(Item(amazon, 1))

        account = Account("email@domain.suffix", Address.Builder().country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build())
    }

    @Test
    fun `should compute subtotal of all items in the cart`() {
        assertThat(shoppingCart.computeSubtotal().toPlainString()).isEqualTo("3838.44")
    }

    @Test
    fun `should return BigDecimal zero when there is nothing in the cart`() {
        assertThat(ShoppingCart().computeSubtotal().toPlainString()).isEqualTo("0.00")
    }

    @Test
    fun `when checking out, build an Order for Physical, another for Digital, and one Order per Membership`() {
        val orders = shoppingCart.checkout(account)
        assertThat(orders.size).isEqualTo(5)

        val groupedOrders = orders
                .asSequence()
                .groupBy { order: Order -> order.type }
        assertThat(groupedOrders[Type.PHYSICAL]?.size).isEqualTo(1)
        assertThat(groupedOrders[Type.DIGITAL]?.size).isEqualTo(1)
        assertThat(groupedOrders[Type.MEMBERSHIP]?.size).isEqualTo(3)
    }
}