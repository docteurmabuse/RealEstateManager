package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.PropertyListContentBinding
import com.openclassrooms.realestatemanager.presentation.ui.property_list.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PropertyAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding =
            PropertyListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val item = values[position]
        holder.binding.property = item
    }

    override fun getItemCount(): Int = values.size

    inner class PropertyViewHolder(
        val binding: PropertyListContentBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    interface PropertyClickListener {
        fun onPropertyClick(view: View, property: PlaceholderItem)
    }
}