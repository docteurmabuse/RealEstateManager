package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.Utils.convertEuroToDollar
import junit.framework.TestCase

class UtilsTest : TestCase() {

    fun testConvertDollarToEuro() {
        val propertyCostInDollars: Int = 100000
        val propertyCostInEuros: Int = 83025
        assertEquals(propertyCostInEuros, convertDollarToEuro(propertyCostInDollars))
    }

    fun testConvertEuroToDollar() {
        val propertyCostInDollars: Int = 100000
        val propertyCostInEuros: Int = 83025
        assertEquals(propertyCostInDollars, convertEuroToDollar(propertyCostInEuros))
    }

    fun testGetTodayDate() {}

    fun testIsInternetAvailable() {}
}