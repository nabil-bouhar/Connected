package com.example.connected.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectedUtilsTest {

    @Test
    fun filterValidateName() {
        val validNames = listOf("Smith", "Melinda Kowal", "Olivier Émond", "ANNE-SOPHIE")
        val invalidNames = listOf("", "0", "Smith0")

        assertEquals(true, ConnectedUtils.filterValidateName(validNames[0]))
        assertEquals(true, ConnectedUtils.filterValidateName(validNames[1]))
        assertEquals(true, ConnectedUtils.filterValidateName(validNames[2]))
        assertEquals(true, ConnectedUtils.filterValidateName(validNames[3]))
        assertEquals(false, ConnectedUtils.filterValidateName(invalidNames[0]))
        assertEquals(false, ConnectedUtils.filterValidateName(invalidNames[1]))
        assertEquals(false, ConnectedUtils.filterValidateName(invalidNames[2]))
    }
}