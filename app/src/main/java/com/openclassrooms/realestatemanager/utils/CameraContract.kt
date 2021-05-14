package com.openclassrooms.realestatemanager.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    override fun parseResult(requestCode: Int, resultCode: Int, intent: Intent?): File? {
        return if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAPTURE_IMAGE -> {
                    val photoFile = photoFile ?: return
                    val uri = FileProvider.getUriForFile(
                        this,
                        "com.raywenderlich.placebook.fileprovider",
                        photoFile
                    )
                    revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    val image = getImageWithPath(photoFile.absolutePath)
                    val bitmap = ImageUtils.rotateImageIfRequired(this, image, uri)
                    updateImage(bitmap)
                }
                REQUEST_GALLERY_IMAGE -> if (data != null && data.data != null) {
                    val imageUri = data.data as Uri
                    val image = getImageWithAuthority(imageUri)
                    image.let {
                        val bitmap = ImageUtils.rotateImageIfRequired(this, it, imageUri)
                        updateImage(bitmap)
                    }
                }
            }
        } else {
            null
        }
    }
}