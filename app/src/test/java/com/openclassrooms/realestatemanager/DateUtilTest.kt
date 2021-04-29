package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.DateUtil.dateToString
import com.openclassrooms.realestatemanager.utils.DateUtil.todayDate
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

import java.time.LocalDate
import java.util.*

class DateUtilTest {

    @Test
    @Throws(Exception::class)
    fun testGetTodayDate() {
        assertEquals("29/04/2021", todayDate)
    }

    @Test
    @Throws(Exception::class)
    fun testDateToString() {
        var date = LocalDate.parse("2021-04-29")
        assertEquals("29/04/2021", dateToString(Date()))
    }
}