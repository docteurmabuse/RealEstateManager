package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import com.openclassrooms.realestatemanager.utils.Utils.todayDate
import junit.framework.TestCase
import org.junit.Test

class UtilsTest : TestCase() {

    @Test
    fun testConvertDollarToEuro() {
        val propertyCostInDollars: Int = 100000
        val propertyCostInEuros: Int = 83025
        assertEquals(propertyCostInEuros, convertDollarToEuro(propertyCostInDollars))
    }

    @Test
    fun testConvertEuroToDollar() {
        val propertyCostInDollars: Int = 100000
        val propertyCostInEuros: Int = 83025
        assertEquals(propertyCostInDollars, convertEuroToDollar(propertyCostInEuros))
    }

    @Test
    fun testGetTodayDate() {
        val myTodayDate: String = "22/04/2021"
        assertEquals(myTodayDate, todayDate)
    }

    fun testIsInternetAvailable(

    ) {
    }


}