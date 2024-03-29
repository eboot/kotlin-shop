package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

class ShoppingCart {

    val items = HashMap<Product, Item>()

    fun addProduct(product: Product, quantity: Int): ShoppingCart {
        require(quantity > 0) { "Quantity must be > 0" }
        if (items.containsKey(product)) {
            val item = items[product]!!
            item.quantity += quantity
        } else {
            items[product] = Item(product, quantity)
        }
        return this
    }

    fun delete(product: Product): ShoppingCart {
        items.remove(product)
        return this
    }

    fun subtotal(): BigDecimal {
        return items.values.asSequence()
                .map(Item::subtotal)
                .fold(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.UNNECESSARY)
    }

    fun checkout(acct: Account): List<Order> {
        return items.values
                .asSequence()
                .groupBy { item -> item.type }
                .map { (group, items) ->
                    when (group) {
                        ItemType.MEMBERSHIP -> items.map { item -> MembershipOrder(item, acct, acct.getDefaultPaymentMethod()) }
                        ItemType.DIGITAL -> listOf(DigitalOrder(items, acct, acct.getDefaultPaymentMethod()))
                        else -> listOf(PhysicalOrder(items, acct, acct.getDefaultPaymentMethod()))
                    }
                }.flatten()
    }
}
