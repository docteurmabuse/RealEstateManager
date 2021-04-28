package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import com.openclassrooms.realestatemanager.utils.Utils.todayDate
import junit.framework.TestCase
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val myTodayDate = current.format(formatter)
        assertEquals(myTodayDate, todayDate)
    }

    fun testIsInternetAvailable() {

    }


}