package com.openclassrooms.realestatemanager.presentation.ui.binding

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.openclassrooms.realestatemanager.R


class ViewBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindImage(imgView: ImageView, imgPath: String?) {
            imgPath?.let {
                // val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

                imgView.load(imgPath.toUri()) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_baseline_broken_image_24dp)
                }
            }
        }
    }
}