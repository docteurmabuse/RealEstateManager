package com.openclassrooms.realestatemanager.presentation.ui.binding

import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import coil.load
import coil.transform.CircleCropTransformation
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.utils.DateUtil.longDateToString
import com.openclassrooms.realestatemanager.utils.DateUtil.stringToLongDate
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import timber.log.Timber
import java.text.NumberFormat
import java.util.*


class ViewBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("cardCollapsed")
        fun bindCollapsableCardView(cardView: CardView, button: ImageButton) {
            if (cardView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    cardView, AutoTransition()
                )
                cardView.visibility = View.GONE
                // constraintLayout.visibility=View.GONE
                button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    cardView, AutoTransition()
                )
                cardView.visibility = View.VISIBLE
                //  constraintLayout.visibility=View.VISIBLE
                button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }

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
        @BindingAdapter("imageCirclePath")
        fun bindCircleImage(imgView: ImageView, imgPath: String?) {
            imgPath?.let {
                imgView.load(imgPath) {
                    placeholder(R.drawable.loading_animation)
                    transformations(CircleCropTransformation())
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
        @BindingAdapter("price", "currency")
        fun bindGetPriceText(textView: TextView, price: Int?, currency: Utils.PropertyCurrency) {
            price?.let {
                Timber.d("PRICE: $it")
                when (currency) {
                    Utils.PropertyCurrency.DOLLAR -> {
                        textView.text = NumberFormat.getCurrencyInstance(Locale.US).format(price)
                    }
                    Utils.PropertyCurrency.EURO -> {
                        textView.text = NumberFormat.getCurrencyInstance(Locale.FRANCE)
                            .format(convertDollarToEuro(price))
                    }
                }
            }
        }

        @JvmStatic
        @BindingAdapter("int")
        fun bindSetIntText(textView: TextView, int: Int?) {
            int?.let { it ->
                Timber.d("PRICE: $it")
                @Suppress("SENSELESS_COMPARISON")
                if (it != null) {
                    textView.text = it.toString()

                } else {
                    textView.text = ""
                }
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
        fun bindLongToText(textView: TextView, long: Long?) {
            long?.let {
                textView.text = longDateToString(it)
            }
        }

        @JvmStatic
        @BindingAdapter("textToDate")
        fun bindTextToDate(textView: TextView, string: String?) {
            string?.let {
                if (it.isBlank() || it.isEmpty())
                    textView.text = ""
                else {
                    textView.text = longDateToString(it.toLong())
                }
            }
        }


        @JvmStatic
        @BindingAdapter("textLongToDate")
        fun bindLongTextToDate(textView: TextView, string: String?) {
            string?.let {
                if (it.isNotEmpty())
                    textView.text = stringToLongDate(it).toString()
                else {
                    textView.text = ""
                }
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

        @JvmStatic
        @BindingAdapter("items")
        fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>?) {
            if (recyclerView.adapter is BindableAdapter<*>) {
                items?.let {
                    (recyclerView.adapter as BindableAdapter<T>).submitList(it)
                }
            }
        }


        @BindingAdapter("valueAttrChanged")
        fun AutoCompleteTextView.setListener(listener: InverseBindingListener?) {
            onItemClickListener = listener?.let {
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    setTag(R.id.type_dropdown, position)
                    it.onChange()
                }
            }
        }

        @JvmStatic
        @get:InverseBindingAdapter(attribute = "value")
        @set:BindingAdapter("value")
        var AutoCompleteTextView.selectedValue: Any?
            get() = if (listSelection != ListView.INVALID_POSITION) adapter.getItem(listSelection) else null
            set(value) {
                val newValue = value ?: adapter.getItem(0)
                setText(newValue.toString(), true)
                if (adapter is ArrayAdapter<*>) {
                    val position = (adapter as ArrayAdapter<Any?>).getPosition(newValue)
                    listSelection = position
                }
            }

        @JvmStatic
        @BindingAdapter("entries", "itemLayout", "textViewId", requireAll = false)
        fun AutoCompleteTextView.bindAdapter(
            entries: Array<Any?>,
            @LayoutRes itemLayout: Int?,
            @IdRes textViewId: Int?
        ) {
            val adapter = when {
                itemLayout == null -> {
                    ArrayAdapter(context, R.layout.item_dropdown, entries)
                }
                textViewId == null -> {
                    ArrayAdapter(context, itemLayout, entries)
                }
                else -> {
                    ArrayAdapter(context, itemLayout, textViewId, entries)
                }
            }
            setAdapter(adapter)
        }

    }
}
