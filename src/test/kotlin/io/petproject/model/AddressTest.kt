package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class AddressTest {

    @Test
    fun `should build a valid address`() {
        val address = Address.Builder()
                .country("Brazil ")
                .streetName("Av Paulista, 1000 ")
                .zipCode(" 01000-000")
                .state("SP ")
                .city(" Sao Paulo ")
                .build()
        assertThat(address.city).isEqualTo("Sao Paulo")
        assertThat(address.state).isEqualTo("SP")
        assertThat(address.country).isEqualTo("Brazil")
        assertThat(address.streetName).isEqualTo("Av Paulista, 1000")
        assertThat(address.zipCode).isEqualTo("01000-000")
    }

    @Test
    fun `when country is blank, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Address.Builder()
                    .country("")
                    .streetName("Av Paulista, 1000 ")
                    .zipCode(" 01000-000")
                    .state("SP ")
                    .city(" Sao Paulo ")
                    .build()
        }
        assertThat(ex.message).isEqualTo("Country cannot be empty")
    }

    @Test
    fun `when streetName is blank, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Address.Builder()
                    .country("Brazil ")
                    .streetName("")
                    .zipCode(" 01000-000")
                    .state("SP ")
                    .city(" Sao Paulo ")
                    .build()
        }
        assertThat(ex.message).isEqualTo("Street name cannot be empty")
    }

    @Test
    fun `when zipCode is blank, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Address.Builder()
                    .country("Brazil ")
                    .streetName("Av Paulista, 1000 ")
                    .zipCode("")
                    .state("SP ")
                    .city(" Sao Paulo ")
                    .build()
        }
        assertThat(ex.message).isEqualTo("ZipCode cannot be empty")
    }


    @Test
    fun `when state is blank, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Address.Builder()
                    .country("Brazil ")
                    .streetName("Av Paulista, 1000 ")
                    .zipCode(" 01000-000")
                    .state("")
                    .city(" Sao Paulo ")
                    .build()
        }
        assertThat(ex.message).isEqualTo("State cannot be empty")
    }

    @Test
    fun `when city is blank, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Address.Builder()
                    .country("Brazil ")
                    .streetName("Av Paulista, 1000 ")
                    .zipCode(" 01000-000")
                    .city("")
                    .state("SP ")
                    .build()
        }
        assertThat(ex.message).isEqualTo("City cannot be empty")
    }

    @Test
    fun `when any of the properties are missing, throw UninitializedPropertyAccessEx`() {
        assertThrows(UninitializedPropertyAccessException::class.java) {
            Address.Builder()
                    .country("Brazil ")
                    .city("Sao Paulo")
                    .state("SP ")
                    .build()
        }
    }

}