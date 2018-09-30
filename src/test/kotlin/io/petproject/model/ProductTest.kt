package io.petproject.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ProductTest {

    @TestFactory
    fun `Product Tests`() = listOf(
            DynamicTest.dynamicTest("when name is blank, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) { Product(" ", Category.PHYSICAL, 1.99) }
            },
            DynamicTest.dynamicTest("when price is lower than or equalTo 0, throw IllegalArgEx") {
                assertThrows(IllegalArgumentException::class.java) { Product("product", Category.PHYSICAL, 0.0) }
            },
            DynamicTest.dynamicTest("when price has more than 2 digits, round up") {
                val product = Product("product", Category.PHYSICAL, 1.965)
                Assertions.assertThat(product.price.toPlainString()).isEqualTo("1.97")
            }
    )

}
