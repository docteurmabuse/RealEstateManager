@file:JvmName("Converter")

package com.openclassrooms.realestatemanager.utils.converter

import android.widget.EditText
import androidx.databinding.InverseMethod
import com.google.common.primitives.Ints
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

object Converter {

    /* @InverseMethod("stringToDate")
     @JvmStatic
     fun dateToString(
         view: EditText, oldValue: Long,
         value: Long
     ): String? {
         return if (oldValue != value) {
             DateUtil.longDateToString(value)
         } else DateUtil.longDateToString(oldValue)
     }

     @JvmStatic
     fun stringToDate(
         view: EditText, oldValue: String,
         value: String
     ): Long? {
         return if (oldValue != value) {
             DateUtil.stringToLongDate(value)
         } else DateUtil.stringToLongDate(oldValue)
     }*/


    @JvmStatic
    @InverseMethod("intToString")
    fun stringToInt(v: EditText, old: Int, new: String): Int {

        return if (old.toString() != new) {
            new.toInt()
        } else old.toInt()
    }

    @JvmStatic
    fun intToString(v: EditText, old: Int, new: Int): String {
        return if (old != new) {
            new.toString()
        } else old.toString()
    }

    @JvmStatic
    @InverseMethod("localPriceToString")
    fun stringToLocalPrice(v: EditText, old: Int, new: String): Int? {
        var number: Int = 0
        try {
            number = if (old.toString() != new && new.isNotEmpty()) {
                Ints.checkedCast(NumberFormat.getInstance(Locale.US).parse(new) as Long)
            } else old.toInt()
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return number
    }

    @JvmStatic
    fun localPriceToString(v: EditText, old: Int, new: Int): String {
        return if (old != new) {
            NumberFormat.getInstance(Locale.US).format(new).toString()
        } else NumberFormat.getInstance(Locale.US).format(old).toString()

    }
}
