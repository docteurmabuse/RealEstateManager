package com.openclassrooms.realestatemanager.presentation.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.property.Media
import timber.log.Timber


class ViewBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindImage(imgView: ImageView, imgPath: String?) {
            imgPath?.let {
                imgView.load(imgPath) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_baseline_broken_image_24dp)
                }

            }
        }

        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindPhotoImage(imgView: ImageView, photos: List<Media.Photo>?) {
            photos?.let {
                Timber.d("PHOTOS: $it")

                if (photos.isNotEmpty())
                    imgView.load(photos[0].photoPath) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_baseline_broken_image_24dp)
                    }
                else {
                    imgView.load(R.drawable.ic_search_24dp) {
                        error(R.drawable.ic_baseline_broken_image_24dp)
                    }
                }
            }
        }
    }
}