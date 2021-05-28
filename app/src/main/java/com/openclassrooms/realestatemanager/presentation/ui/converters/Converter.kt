package com.openclassrooms.realestatemanager.presentation.ui.converters

import android.widget.EditText
import androidx.databinding.InverseMethod
import com.openclassrooms.realestatemanager.utils.DateUtil

object Converter {
    @InverseMethod("stringToDate")
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
    ): Long {
        return if (oldValue != value) {
            DateUtil.stringToDate(value)
        } else DateUtil.longToString(oldValue)
    }

}