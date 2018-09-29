package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ShoppingCartTest {

    private val shoppingCart = ShoppingCart()

    @BeforeAll
    fun `setup`() {
        val console = Product("game console", ProductType.PHYSICAL, 1899.00)
        val netflix = Product("netflix voucher", ProductType.DIGITAL, 25.00)
        val amazonPrime = Product("amazon video prime subscription", ProductType.SUBSCRIPTION, 14.90)
        val physicalBook = Product("cracking the code interview", ProductType.PHYSICAL_TAX_FREE, 219.57)

        shoppingCart.addItem(Item(console, 1))
                .addItem(Item(netflix, 4))
                .addItem(Item(amazonPrime, 1))
                .addItem(Item(physicalBook, 2))
    }

    @Test
    fun `should compute subtotal of all items in the cart`() {
        assertThat(shoppingCart.computeSubtotal().toPlainString()).isEqualTo("2453.04")
    }

    @Test
    fun `should return BigDecimal zero when there is nothing in the cart`() {
        assertThat(ShoppingCart().computeSubtotal().toPlainString()).isEqualTo("0.00")
    }

}