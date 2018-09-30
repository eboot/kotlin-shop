package com.creditas.model

interface PaymentMethod {

    val billingAddress: Address

    fun charge(): Boolean
}