package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class PhotoOptionDialogFragment : DialogFragment() {

    //Create dialog fragment to choose photo retrieved method
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Dynamic indices because position of the Gallery and Camera may change based on device capabilities
        var captureSelectIdx = -1
        var pickSelectIdx = -1

        val options = ArrayList<String>()

        val context = activity as Context

        if (canCapture(context)) {
            options.add("Camera")
            captureSelectIdx = 0
        }

        if (canPick(context)) {
            options.add("Gallery")
            pickSelectIdx = if (captureSelectIdx == 0) 1 else 0
        }
        return AlertDialog.Builder(context)
            .setTitle("Photo Option")
            .setItems(options.toTypedArray<CharSequence>()) { _, which ->
                if (which == captureSelectIdx) {
                    val result = "capture"
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                } else if (which == pickSelectIdx) {
                    val result = "pick"
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        fun canPick(context: Context): Boolean {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            return (pickIntent.resolveActivity(
                context.packageManager
            ) != null)
        }

        fun canCapture(context: Context): Boolean {
            val captureIntent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
            )
            return (captureIntent.resolveActivity(
                context.packageManager
            ) != null)
        }

        fun newInstance(context: Context) =
            if (canPick(context) || canCapture(context)) {
                PhotoOptionDialogFragment()
            } else {
                null
            }
    }
}
