package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

    @Throws(IOException::class)
    fun createUniqueImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale(Locale.US)).format(Date())
        val fileName = "REM_" + timestamp + "_"
        val fileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", fileDir)
    }
}