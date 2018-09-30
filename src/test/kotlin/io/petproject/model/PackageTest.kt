package io.petproject.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PackageTest {

    private lateinit var address: Address

    @BeforeEach
    fun setup() {
        address = Address.Builder()
                .country("Brazil")
                .city("Sao Paulo")
                .state("SP")
                .zipCode("01000-000")
                .streetName("Av Paulista, 1000")
                .build()
    }

    @Test
    fun `when creating a Package, there must be at least one item to package`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Package(ArrayList(), address)
        }
        Assertions.assertThat(ex.message).isEqualTo("There must be at least one item to package")
    }

}