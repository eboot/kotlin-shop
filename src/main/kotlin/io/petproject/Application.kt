package io.petproject

import com.google.gson.GsonBuilder
import io.petproject.model.*


fun main(args: Array<String>) {

    val address = Address.Builder()
            .country("Brazil")
            .city("Sao Paulo")
            .state("SP")
            .zipCode("01000-000")
            .streetName("Av Paulista, 1000")
            .build()

    val account = Account("email@domain.suffix", address)

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

    val orders = shoppingCart.checkout(account)
    val paymentMethod = getPaymentMethod(address)
    val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

    orders.forEach { order ->
        run {
            (order as? PhysicalOrder)?.shippingAddress(address)
            order.paymentMethod(paymentMethod)
            order.place()
            order.pay()
        }

        run {
            val orderType = order.invoice!!.items[0].group
            println("====================  $orderType ORDER INVOICE =========================")
            println(gson.toJson(order.invoice))
        }
    }

}

fun getPaymentMethod(billingAddress: Address): PaymentMethod {
    return object : PaymentMethod {
        override val billingAddress: Address = billingAddress
        override fun charge(): Boolean = true
    }
}
