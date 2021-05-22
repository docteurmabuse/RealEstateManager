package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import java.io.File

class LocationContract : ActivityResultContract<Int, File?>() {
    private var photoFile: File? = null

    override fun createIntent(context: Context, input: Int?): Intent {
        TODO("Not yet implemented")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): File? {
        if (resultCode != REQUEST_CODE_AUTOCOMPLETE) {
            return null
        }
        return null
    }

    override fun getSynchronousResult(context: Context, input: Int?): SynchronousResult<File?>? {
        return super.getSynchronousResult(context, input)
    }


}