package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.MediaItemContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media

class PhotoListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media.Photo>() {

        override fun areItemsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
            TODO("not implemented")
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            MediaItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Media.Photo>) {
        differ.submitList(list)
    }

    class PhotoViewHolder
    constructor(
        private val binding: MediaItemContentBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media.Photo) = with(binding.root) {
            binding.deleteButton.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item)
            }
            binding.photo = item
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Media.Photo)
    }
}