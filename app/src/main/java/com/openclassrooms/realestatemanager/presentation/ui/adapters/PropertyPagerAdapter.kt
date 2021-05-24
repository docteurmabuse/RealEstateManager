package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.PropertyItemLayoutBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media

class PropertyPagerAdapter : ListAdapter<Media.Photo, PropertyPagerAdapter.PagerViewHolder>(
    ITEM_COMPARATOR
) {
    private var _binding: PropertyItemLayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PropertyPagerAdapter.PagerViewHolder {
        _binding =
            PropertyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.photo = item
    }

    inner class PagerViewHolder(
        val binding: PropertyItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Media.Photo>() {
    override fun areItemsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
        return oldItem.photoPath == newItem.photoPath
    }

    override fun areContentsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
        return oldItem == newItem
    }
}
