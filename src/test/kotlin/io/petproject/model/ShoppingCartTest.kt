package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ShoppingCartTest {

    private val shoppingCart = ShoppingCart()
    private lateinit var account: Account

    @BeforeEach
    fun `setup`() {
        val console = Product("PS4 Slim 1TB", Category.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", Category.PHYSICAL, 399.00)
        val netflix = Product("Netflix Familiar Plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("Spotify Premium", Category.SUBSCRIPTION, 14.90)
        val amazon = Product("Amazon Prime", Category.SUBSCRIPTION, 12.90)
        val book = Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00)
        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)

        shoppingCart.addProduct(console, 1)
                .addProduct(chair, 2)
                .addProduct(book, 2)
                .addProduct(anotherBook, 1)
                .addProduct(musicDigitalAlbum, 1)
                .addProduct(videoGameDigitalCopy, 4)
                .addProduct(netflix, 1)
                .addProduct(spotify, 1)
                .addProduct(amazon, 1)

        account = Account("email@domain.suffix", Address.Builder().country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build())
    }

    @Test
    fun `when computing Subtotal, sum the price of all items in the cart`() {
        assertThat(shoppingCart.subtotal().toPlainString()).isEqualTo("3838.44")
    }

    @Test
    fun `when computing Subtotal, return Zero if there's nothing in the cart`() {
        assertThat(ShoppingCart().subtotal().toPlainString()).isEqualTo("0.00")
    }

    @Test
    fun `when adding a Product that is already in the Cart, add up to the quantity`() {
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)
        shoppingCart.addProduct(videoGameDigitalCopy, 10)
        assertThat(shoppingCart.items[videoGameDigitalCopy]!!.quantity).isEqualTo(14)
    }

    @Test
    fun `when adding a Product with quantity lowerThan or equalTo 0, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            shoppingCart.addProduct(Product("product", Category.PHYSICAL, 1.90), 0)
        }
        assertThat(ex.message).isEqualTo("Quantity must be > 0")
    }

    @Test
    fun `when checking out, build an Order for Physical, another for Digital, and one Order per Membership`() {
        val orders = shoppingCart.checkout(account)
        assertThat(orders.size).isEqualTo(5)
        assertThat(orders.filter { order -> order is PhysicalOrder }.size).isEqualTo(1)
        assertThat(orders.filter { order -> order is DigitalOrder }.size).isEqualTo(1)
        assertThat(orders.filter { order -> order is MembershipOrder }.size).isEqualTo(3)
    }
}