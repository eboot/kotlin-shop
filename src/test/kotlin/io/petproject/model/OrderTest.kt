package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OrderTest {

    private var physicalItems = ArrayList<Item>()
    private var physicalTaxFreeItems = ArrayList<Item>()
    private var digitalItems = ArrayList<Item>()
    private var subscriptions = ArrayList<Item>()
    private var mixedItems = ArrayList<Item>()
    private lateinit var account: Account

    @BeforeAll
    fun setup() {
        account = Account("email@domain.suffix", Address.Builder()
                .country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build()
        )

        val console = Product("game console", Category.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", Category.PHYSICAL, 399.00)
        physicalItems.addAll(listOf(
                Item(console, 1),
                Item(chair, 2)
        ))

        val netflix = Product("netflix familiar plan", Category.SUBSCRIPTION, 29.90)
        val spotify = Product("spotify premium", Category.SUBSCRIPTION, 14.90)
        subscriptions.addAll(listOf(
                Item(netflix, 1),
                Item(spotify, 1)
        ))

        val book = Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00)
        physicalTaxFreeItems.addAll(listOf(
                Item(book, 2),
                Item(anotherBook, 1)
        ))

        val musicDigitalAlbum = Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90)
        digitalItems.addAll(listOf(
                Item(musicDigitalAlbum, 1),
                Item(videoGameDigitalCopy, 4)
        ))

        mixedItems.addAll(physicalItems)
        mixedItems.addAll(digitalItems)
    }

    @Test
    fun `when creating a Order with Items from different ItemGroups, throw IllegalStateEx`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            Order(mixedItems, account)
        }
        assertThat(ex.message).isEqualTo("Items must belong to the same Item Group")
    }

    @Test
    fun `when creating an Order, there must be at least one item in the list`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Order(ArrayList(), account)
        }
        assertThat(ex.message).isEqualTo("There must be at least one item to place the Order")
    }


}