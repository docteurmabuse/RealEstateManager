package com.openclassrooms.realestatemanager.presentation.ui.binding

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotosAdapter
import com.openclassrooms.realestatemanager.utils.DateUtil.dateToString
import timber.log.Timber
import java.text.NumberFormat
import java.util.*


class ViewBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindImage(imgView: ImageView, imgPath: String?) {
            imgPath?.let {
                Timber.d("PHOTO: $imgPath")
                imgView.load(imgPath) {
                    placeholder(R.drawable.loading_animation)
                    crossfade(true)
                    error(R.drawable.ic_baseline_broken_image_24dp)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("imagePath")
        fun bindPhotoImage(imgView: ImageView, photos: List<Media.Photo>?) {
            photos?.let {
                Timber.d("PHOTOS: $it")
                if (it.isNotEmpty()) {
                    val imgPath = it[0].photoPath
                    imgView.load(imgPath) {
                        placeholder(R.drawable.loading_image)
                        crossfade(true)
                        error(R.drawable.ic_baseline_broken_image_24dp)
                        Timber.d("PHOTO: $imgPath")
                    }
                } else {
                    imgView.load(R.drawable.ic_search_24dp) {
                        error(R.drawable.ic_baseline_broken_image_24dp)
                    }
                }
            }
        }

        @JvmStatic
        @BindingAdapter("price")
        fun bindGetPriceText(textView: TextView, price: Int?) {
            price?.let {
                Timber.d("PRICE: $it")
                textView.text = NumberFormat.getCurrencyInstance(Locale.US).format(price)
            }
        }

        @JvmStatic
        @BindingAdapter("price")
        fun bindSetPriceText(textView: TextView, price: Int?) {
            price?.let {
                Timber.d("PRICE: $it")
                textView.text = NumberFormat.getCurrencyInstance(Locale.US).format(price)
            }
        }

        @JvmStatic
        @BindingAdapter("isVisible")
        fun bindVisibilityView(view: View, isVisible: Boolean?) {
            isVisible?.let {
                if (isVisible) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        }

        @JvmStatic
        @BindingAdapter("dateToText")
        fun bindDateText(textView: TextView, date: Date?) {
            date?.let {
                textView.text = dateToString(it)
            }
        }

        @BindingAdapter("clearOnFocusAndDispatch")
        @JvmStatic
        fun clearOnFocusAndDispatch(view: EditText, listener: View.OnFocusChangeListener?) {
            view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
                val textView = focusedView as TextView
                if (hasFocus) {
                    // Delete contents of the EditText if the focus entered.
                    view.setTag(R.id.previous_value, textView.text)
                    textView.text = ""
                } else {
                    if (textView.text.isEmpty()) {
                        val tag: CharSequence? =
                            textView.getTag(R.id.previous_value) as CharSequence
                        textView.text = tag ?: ""
                    }
                    // If the focus left, update the listener
                    listener?.onFocusChange(focusedView, hasFocus)
                }
            }
        }

        @BindingAdapter("app:items")
        @JvmStatic
        fun setItems(listView: RecyclerView, items: List<Media.Photo>?) {
            items?.let {
                if (listView.adapter != null)
                    (listView.adapter as PhotosAdapter).submitList(items)
            }
        }
    }
}
