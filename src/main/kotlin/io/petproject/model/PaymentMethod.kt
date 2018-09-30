package io.petproject.model

interface PaymentMethod {

    val billingAddress: Address

    fun charge(): Boolean
}