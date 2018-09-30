package io.petproject.model

import java.math.BigDecimal
import java.math.RoundingMode

class ShoppingCart {

    var items = HashSet<Item>()

    fun addItem(item: Item): ShoppingCart {
        items.add(item)
        return this
    }

    fun removeItem(item: Item): ShoppingCart {
        items.remove(item)
        return this
    }

    fun computeSubtotal(): BigDecimal {
        return items.stream()
                .map(Item::subtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.UNNECESSARY)
    }

    fun checkout(acct: Account): List<Order> {
        return items.asSequence()
                .groupBy { item -> item.group }
                .map { (group, items) ->
                    when (group) {
                        ItemType.MEMBERSHIP -> items.map { item -> MembershipOrder(item, acct, acct.getDefaultPaymentMethod()) }
                        ItemType.DIGITAL -> listOf(DigitalOrder(items, acct, acct.getDefaultPaymentMethod()))
                        else -> listOf(PhysicalOrder(items, acct, acct.getDefaultPaymentMethod()))
                    }
                }.flatten()
    }
}
