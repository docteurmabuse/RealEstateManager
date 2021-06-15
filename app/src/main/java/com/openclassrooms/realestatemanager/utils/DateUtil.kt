package com.openclassrooms.realestatemanager.utils

import android.widget.DatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    val todayDate: String
        get() {
            return dateToLong(createTimestamp()).toString()
        }

    fun longToDate(long: Long?): Date? {
        return if (long != null) Date(long)
        else null
    }

    fun longDateToString(long: Long?): String? {

        return longToDate(long)?.let { dateToString(it) }
    }

    fun stringToLongDate(string: String?): Long? {
        return if (string != "null" || string != "")
            string?.let { dateToLong(stringToDate(it)) }
        else return 0L
    }

    private fun dateToLong(date: Date?): Long? {
        return if (date != null)
            date.time / 1000
        else null
    }

    fun dateToString(date: Date): String {
        return try {
            sdf.format(date)
        } catch (e: NumberFormatException) {
            return ""
        }
    }

    fun stringToDate(string: String): Date? {
        return try {
            string.let {
                sdf.parse(string)
                    ?: throw NullPointerException("Could not convert date string to Date object.")
            }
        } catch (e: ParseException) {
            null
        }
    }

    fun createTimestamp(): Date {
        return Date()
    }

    fun DatePicker.getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

}
