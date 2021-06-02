@file:JvmName("Converter")

package com.openclassrooms.realestatemanager.utils.converter

import android.content.res.Resources
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.InverseMethod
import com.google.common.primitives.Ints
import timber.log.Timber
import java.io.IOException
import java.text.DecimalFormat
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
    @InverseMethod("toDouble")
    fun toString(
        view: TextView, oldValue: Int,
        value: Int
    ): String? {
        val numberFormat = getNumberFormat(view)
        try {
            // Don't return a different value if the parsed value
            // doesn't change
            val inView = view.text.toString()
            val parsed = numberFormat.parse(inView).toInt()
            if (parsed == value) {
                return view.text.toString()
            }
        } catch (e: ParseException) {
            // Old number was broken
        }
        return numberFormat.format(value)
    }

    @JvmStatic
    fun toDouble(
        view: TextView, oldValue: Int,
        value: String?
    ): Int {
        val numberFormat = getNumberFormat(view)
        return try {
            numberFormat.parse(value).toInt()
        } catch (e: ParseException) {
            val resources: Resources = view.resources
            val errStr: String = "Bad Number"
            view.error = errStr
            return oldValue
        }
    }

    private fun getNumberFormat(view: View): NumberFormat {
        val resources: Resources = view.resources
        val locale: Locale = resources.configuration.locale
        val format = NumberFormat.getNumberInstance(locale)
        if (format is DecimalFormat) {
            val decimalFormat: DecimalFormat = format
            decimalFormat.isGroupingUsed = false
        }
        return format
    }

    @JvmStatic
    fun stringToInt(v: EditText, old: Int, new: String): Int {
        return try {
            if (old.toString() != new) {
                if (new == "") {
                    0
                } else {
                    new.toInt()
                }
            } else {
                0
            }
        } catch (e: IOException) {
            val errStr: String = "error"
            v.error = errStr
            old
        }
    }

    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(v: EditText, old: Int, new: Int): String {
        val result: String
        return try {
            result = if (old != new) {
                v.text.toString()
            } else {
                v.text.toString()
            }
            result
        } catch (e: IOException) {
            Timber.e("Error")
            old.toString()
        }

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
