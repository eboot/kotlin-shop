package io.petproject.model

import java.math.BigDecimal

class ShoppingCart {

    var items = HashSet<Item>()

    fun addItem(item: Item): ShoppingCart {
        items.add(item)
        return this
    }

    fun remoteItem(item: Item): ShoppingCart {
        items.remove(item)
        return this
    }

    fun computeSubtotal(): BigDecimal {
        return BigDecimal.ZERO
    }
}
