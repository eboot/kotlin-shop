package io.petproject.model

data class Account(val email: String, val address: Address) {

    var wallet: ArrayList<PaymentMethod> = ArrayList()

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        wallet.add(paymentMethod)
    }

    fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        wallet.remove(paymentMethod)
    }

    fun getAllPaymentMethods(): List<PaymentMethod> {
        return wallet
    }

    fun getDefaultPaymentMethod(): PaymentMethod? {
        return if (wallet.isNotEmpty()) wallet[0] else null
    }

}