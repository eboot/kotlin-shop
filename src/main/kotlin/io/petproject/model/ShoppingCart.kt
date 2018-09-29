package io.petproject.model

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

    fun computeSubtotal() {

    }
}
