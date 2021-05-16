package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.MediaItemContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media.Photo

class PhotosAdapter(private val photos: ArrayList<Photo>) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosAdapter.PhotosViewHolder {
        val binding =
            MediaItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapter.PhotosViewHolder, position: Int) {
        val items = photos[position]
        holder.binding.photo
    }

    override fun getItemCount(): Int = photos.size

    fun addData(list: ArrayList<Photo>) {
        photos.addAll(list)
    }

    inner class PhotosViewHolder(
        val binding: MediaItemContentBinding
    ) : RecyclerView.ViewHolder(binding.root)
}