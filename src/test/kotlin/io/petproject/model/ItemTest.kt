package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ItemTest {

    @TestFactory
    fun `Product Tests`() = listOf(
            DynamicTest.dynamicTest("when name is blank, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) { Product(" ", ProductType.PHYSICAL, 1.99) }
            },
            DynamicTest.dynamicTest("when price is lower than or equalTo 0, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) { Product("product", ProductType.PHYSICAL, 0.0) }
            },
            DynamicTest.dynamicTest("when price has more than 2 digits, round up") {
                val product = Product("product", ProductType.PHYSICAL, 1.965)
                assertThat(product.price).isEqualTo(1.97)
            }
    )

    @TestFactory
    fun `Item Tests`() = listOf(
            DynamicTest.dynamicTest("when quantity is lower than or equalTo 0, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) {
                    val product = Product("product", ProductType.PHYSICAL, 1.99)
                    Item(product, 0)
                }
            },

            DynamicTest.dynamicTest("when querying for subtotal, compute unit_price * units") {
                val product = Product("product", ProductType.PHYSICAL, 1.99)
                val item = Item(product, 10)
                assertThat(item.subtotal).isEqualTo(19.90)
            }
    )
}