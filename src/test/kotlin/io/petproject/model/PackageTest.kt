package com.creditas.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class PackageTest {

    @Test
    fun `when creating a Package, there must be at least one item to package`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Package(ArrayList())
        }
        Assertions.assertThat(ex.message).isEqualTo("There must be at least one item to package")
    }

}