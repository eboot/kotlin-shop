package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class ItemTest {

    @Test
    fun `when quantity is lower than or equalTo 0, throw IllegalArgEx`() {
        assertThrows(IllegalArgumentException::class.java) {
            val product = Product("product", Category.PHYSICAL, 1.99)
            Item(product, 0)
        }
    }

    @Test
    fun `when querying for subtotal, compute unit_price x units`() {
        val product = Product("product", Category.PHYSICAL, 1.99)
        val item = Item(product, 10)
        assertThat(item.subtotal.toPlainString()).isEqualTo("19.90")
    }

    @Test
    fun `when product category is a digital copy, item_group must be Digital`() {
        listOf(Category.DIGITAL_MUSIC,
                Category.DIGITAL_SOFTWARE,
                Category.DIGITAL_COPY_MOVIES_TV,
                Category.DIGITAL_VIDEO_GAMES
        ).map { category ->
            Item(Product("product", category, 10.00), 1)
        }.forEach { item ->
            run { assertThat(item.group).isEqualTo(Type.DIGITAL) }
        }
    }

    @Test
    fun `when product category is of physical type, item_group must be Physical`() {
        listOf(Category.PHYSICAL,
                Category.PHYSICAL_BOOK
        ).map { category ->
            Item(Product("product", category, 10.00), 1)
        }.forEach { item ->
            run { assertThat(item.group).isEqualTo(Type.PHYSICAL) }
        }
    }

    @Test
    fun `when product category is a subscription, item_group must be Membership`() {
        val item = Item(Product("product", Category.SUBSCRIPTION, 10.00), 1)
        assertThat(item.group).isEqualTo(Type.MEMBERSHIP)
    }

}