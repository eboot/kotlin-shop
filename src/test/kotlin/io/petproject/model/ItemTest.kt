package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ItemTest {

    @TestFactory
    fun `Item Tests`() = listOf(
            DynamicTest.dynamicTest("when quantity is lower than or equalTo 0, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) {
                    val product = Product("product", Category.PHYSICAL, 1.99)
                    Item(product, 0)
                }
            },

            DynamicTest.dynamicTest("when querying for subtotal, compute unit_price * units") {
                val product = Product("product", Category.PHYSICAL, 1.99)
                val item = Item(product, 10)
                assertThat(item.subtotal.toPlainString()).isEqualTo("19.90")
            }
    )
}