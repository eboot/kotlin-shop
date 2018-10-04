package io.petproject.e2e

import io.petproject.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class E2ETest {

    @Test
    fun `end to end tests with model`() {
        val shoppingCart = ShoppingCart()
                .addProduct(Product("PS4 Slim 1TB", Category.PHYSICAL, 1899.00), 1)
                .addProduct(Product("PDP Chair", Category.PHYSICAL, 399.00), 2)
                .addProduct(Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57), 2)
                .addProduct(Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00), 1)
                .addProduct(Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00), 1)
                .addProduct(Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90), 4)
                .addProduct(Product("Netflix Familiar Plan", Category.SUBSCRIPTION, 29.90), 1)
                .addProduct(Product("Spotify Premium", Category.SUBSCRIPTION, 14.90), 1)
                .addProduct(Product("Amazon Prime", Category.SUBSCRIPTION, 12.90), 1)

        val address = getAddress()
        val paymentMethod = getPaymentMethod()

        val account = Account("email@domain.suffix", address)
        val orders = shoppingCart.checkout(account)

        orders.forEach { order ->
            run {
                assertThat(order.status).isEqualTo(OrderStatus.UNKNOWN)
                (order as? PhysicalOrder)?.shippingAddress(address)
                order.paymentMethod(paymentMethod)

                order.place()
                assertThat(order.status).isEqualTo(OrderStatus.PENDING)

                order.pay()
                assertThat(order.status.code).isEqualTo(OrderStatus.UNSHIPPED.code)

                order.fulfill()
                if (order is MembershipOrder) {
                    assertThat(order.status.code).isEqualTo(OrderStatus.ACTIVATED.code)
                } else {
                    assertThat(order.status.code).isEqualTo(OrderStatus.SHIPPED.code)
                    order.complete()
                    assertThat(order.status.code).isEqualTo(OrderStatus.DELIVERED.code)
                }
            }
        }
    }

    private fun getAddress(): Address {
        return Address.Builder()
                .country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build()
    }

    private fun getPaymentMethod(): PaymentMethod {
        return object : PaymentMethod {
            override val billingAddress: Address = getAddress()
            override fun charge(): Boolean = true
        }
    }

}
