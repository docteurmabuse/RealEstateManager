package com.openclassrooms.realestatemanager.presentation.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.openclassrooms.realestatemanager.R


class ViewBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindImage(imgView: ImageView, imgPath: String?) {
            imgPath?.let {
                imgView.load(imgPath.toString()) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_baseline_broken_image_24dp)
                }
            }
        }
    }
}