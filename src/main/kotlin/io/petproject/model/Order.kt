package io.petproject.model

class Order(val items: List<Item>, val account: Account, val payment: Payment) {

    lateinit var billingAddress: Address
    lateinit var shippingAddress: Address

}