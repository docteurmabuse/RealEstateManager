package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.PropertyListContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property_list.placeholder.PlaceholderContent.PlaceholderItem
import timber.log.Timber

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PropertyAdapter(
    private val properties: ArrayList<Property>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding =
            PropertyListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val item = properties[position]
        holder.binding.property = item
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
                Timber.tag("clickTransformation").d("click item : It's OK :$position")
            }
        }
    }

    override fun getItemCount(): Int = properties.size

    fun addData(list: List<Property>) {
        properties.addAll(list)
    }

    inner class PropertyViewHolder(
        val binding: PropertyListContentBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

}