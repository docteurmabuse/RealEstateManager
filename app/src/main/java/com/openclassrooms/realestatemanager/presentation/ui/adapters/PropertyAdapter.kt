package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.PropertyListContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Property
import timber.log.Timber

class PropertyAdapter(
    private val properties: ArrayList<Property>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) : ListAdapter<Property, PropertyAdapter.PropertyViewHolder>(
    ITEM_COMPARATOR
) {

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

    inner class PropertyViewHolder(
        val binding: PropertyListContentBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Property>() {
    override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
        return newItem.description == newItem.description && oldItem.address1 == newItem.address1 && newItem.address2 == newItem.address2
    }
}