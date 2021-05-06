package com.openclassrooms.realestatemanager.utils

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
            return sdf.format(createTimestamp())
        }

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000
    }

    fun dateToString(date: Date): String {
        return sdf.format(date)
    }

    fun stringToDate(string: String): Date {
        return sdf.parse(string)
            ?: throw NullPointerException("Could not convert date string to Date object.")
    }

    fun createTimestamp(): Date {
        return Date()
    }

}