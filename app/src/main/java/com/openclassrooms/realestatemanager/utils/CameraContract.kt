package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import java.io.File

class CameraContract : ActivityResultContract<Int, File?>() {
    private var photoFile: File? = null
    val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
    override fun createIntent(context: Context, input: Int?): Intent {
        TODO("Not yet implemented")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): File? {
        TODO("Not yet implemented")
    }

    override fun getSynchronousResult(context: Context, input: Int?): SynchronousResult<File?>? {
        return super.getSynchronousResult(context, input)
    }


}