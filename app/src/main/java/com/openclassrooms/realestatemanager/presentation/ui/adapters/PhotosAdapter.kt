package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.MediaItemContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media

class PhotosAdapter :
    ListAdapter<Media.Photo, PhotosAdapter.PhotosViewHolder>(
        ITEM_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosAdapter.PhotosViewHolder {
        val binding =
            MediaItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapter.PhotosViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.photo = item
    }

    inner class PhotosViewHolder(
        val binding: MediaItemContentBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Media.Photo>() {
    override fun areItemsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
        return oldItem.photoPath == newItem.photoPath
    }

    override fun areContentsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
        return newItem.photoPath == newItem.name && oldItem.photoPath == newItem.photoPath
    }
}