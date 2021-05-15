package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import java.io.File

class CameraContract : ActivityResultContract<Int, File?>() {
    private var photoFile: File? = null
    val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

    override fun createIntent(context: Context, input: Int?): Intent {
        photoFile = null
        try {
            photoFile = ImageUtils.createUniqueImageFile(context)
        } catch (ex: java.io.IOException) {

        }
        photoFile?.let { photoFile ->
            val photoUri = FileProvider.getUriForFile(
                context, "com.openclassrooms.realestatemanager.fileprovider",
                photoFile
            )
            captureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
            val intentActivities = context.packageManager.queryIntentActivities(
                captureIntent, PackageManager.MATCH_DEFAULT_ONLY
            )
            intentActivities.map {
                it.activityInfo.packageName
            }
                .forEach {
                    context.grantUriPermission(
                        it,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
        }
        return captureIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): File? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return photoFile
    }
}