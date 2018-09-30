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
        val console = Product("game console", Category.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", Category.PHYSICAL, 399.00)
        val netflix = Product("netflix familiar plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("spotify premium", Category.SUBSCRIPTION, 14.90)
        val book = Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00)
        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)

        shoppingCart.addItem(Item(console, 1))
                .addItem(Item(chair, 2))
                .addItem(Item(netflix, 1))
                .addItem(Item(spotify, 1))
                .addItem(Item(book, 2))
                .addItem(Item(anotherBook, 1))
                .addItem(Item(musicDigitalAlbum, 1))
                .addItem(Item(videoGameDigitalCopy, 4))

        account = Account("email@domain.suffix", Address.Builder().country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build())
    }

    @Test
    fun `should compute subtotal of all items in the cart`() {
        assertThat(shoppingCart.computeSubtotal().toPlainString()).isEqualTo("3825.54")
    }

    @Test
    fun `should return BigDecimal zero when there is nothing in the cart`() {
        assertThat(ShoppingCart().computeSubtotal().toPlainString()).isEqualTo("0.00")
    }

    @Test
    fun `should build a list of Orders distinguished by Category`() {
        val orders: List<Order> = shoppingCart.checkout(account)
        assertThat(orders.size).isEqualTo(5)
    }
}