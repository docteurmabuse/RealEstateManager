@file:JvmName("Converter")

package com.openclassrooms.realestatemanager.utils.converter

import android.widget.EditText
import androidx.databinding.InverseMethod

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

    /*  @JvmStatic
      @InverseMethod("intToString")
      fun stringToInt(
          view: EditText, oldValue: String,
          value: Int
      ): Int? {
          return if (oldValue != value.toString()) {
              value.toInt()
          } else oldValue.toInt()

      }
      @JvmStatic
      fun intToString(
          view: EditText, oldValue: Int,
          value: String
      ): String? {
          return if (oldValue.toString() != value) {
             value.toString()
          } else oldValue.toString()
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
}