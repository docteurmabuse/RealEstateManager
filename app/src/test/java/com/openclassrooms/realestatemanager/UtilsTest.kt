package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import junit.framework.TestCase
import org.junit.Test


class UtilsTest : TestCase() {

    @Test
    @Throws(Exception::class)
    fun testConvertDollarToEuro() {
        val propertyCostInDollars = 82700
        val propertyCostInEuros = 100000
        assertEquals(propertyCostInEuros, convertDollarToEuro(propertyCostInDollars))
    }

    @Test
    @Throws(Exception::class)
    fun testConvertEuroToDollar() {
        val propertyCostInDollars = 82700
        val propertyCostInEuros = 100000
        assertEquals(propertyCostInDollars, convertEuroToDollar(propertyCostInEuros))
    }


}