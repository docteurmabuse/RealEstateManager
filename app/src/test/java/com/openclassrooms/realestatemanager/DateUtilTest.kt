package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.DateUtil
import com.openclassrooms.realestatemanager.utils.DateUtil.createTimeStamp
import com.openclassrooms.realestatemanager.utils.DateUtil.dateToString
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtilTest {

    @Test
    @Throws(Exception::class)
    fun testDateToString() {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val todayDate = dateFormat.format(Date())
        assertEquals(todayDate, dateToString(Date()))
    }

    @Test
    @Throws(Exception::class)
    fun testLongDateToString() {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val todayDate = dateFormat.format(Date())
        val longDate = createTimeStamp()
        val formattedDate = DateUtil.longDateToString(longDate)
        assertEquals(todayDate, formattedDate)
    }
}
