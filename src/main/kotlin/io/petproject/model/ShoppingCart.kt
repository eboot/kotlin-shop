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

    fun checkout(account: Account): List<Order> {
        return items.asSequence()
                .groupBy { i -> i.group }
                .map { (group, items) ->
                    if (group == Type.MEMBERSHIP) {
                        items.map { i -> Order(i, account) }
                    } else {
                        listOf(Order(items, account))
                    }
                }.flatten()
    }
}
