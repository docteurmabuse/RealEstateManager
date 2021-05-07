package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

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
        val item: PlaceholderItem = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class PropertyViewHolder(
        private val binding: PropertyListContentBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceholderItem) {
            binding.content.text = item.content
            binding.idText.text = item.details
        }

        val idView: TextView = binding.idText
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    interface PropertyClickListener {
        fun onPropertyClick(view: View, property: String)
    }
}