package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utils.convertDollarToEuro
import junit.framework.TestCase

class UtilsTest : TestCase() {

    fun testConvertDollarToEuro() {
        val propertyCostInDollars: Int = 25000
        val propertyCostInEuros: Int = 20775
        assertEquals(propertyCostInEuros, convertDollarToEuro(propertyCostInDollars))
    }

    fun testConvertEuroToDollar() {}

    fun testGetTodayDate() {}

    fun testIsInternetAvailable() {}
}