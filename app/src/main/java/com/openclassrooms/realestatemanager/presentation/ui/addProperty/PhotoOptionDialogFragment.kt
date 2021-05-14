package com.openclassrooms.realestatemanager.presentation.ui.addProperty

class PhotoOptionDialogFragment : DialogFragment() {
    interface PhotoOptionDialogListener {
        fun onCaptureClick()
        fun onPickClick()
    }

    private lateinit var listener: PhotoOptionDialogListener

    //Create dialog fragment to choose photo retrived method
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = fragment as PhotoOptionDialogListener
        //Dynamic indices because position of the Gallery and Camera may change based on evice capabilities
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
                    listener.onCaptureClick()
                } else if (which == pickSelectIdx) {
                    listener.onPickClick()
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