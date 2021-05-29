package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.MediaItemContentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.presentation.ui.binding.BindableAdapter
import com.openclassrooms.realestatemanager.utils.ItemTouchHelperAdapter
import com.openclassrooms.realestatemanager.utils.OnStartDragListener
import java.util.*

class PhotoListAdapter(
    private val interaction: Interaction? = null,
    private val dragStartListener: OnStartDragListener,
    val onSwiped: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter,
    BindableAdapter<Media.Photo> {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media.Photo>() {

        override fun areItemsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Media.Photo, newItem: Media.Photo): Boolean {
            return oldItem.photoPath == newItem.photoPath && oldItem.name == newItem.name
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    var photoList: ArrayList<Media.Photo> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            MediaItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(
            binding,
            interaction,
            dragStartListener
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

    override fun submitList(list: List<Media.Photo>) {
        differ.submitList(list)
        photoList = list as ArrayList<Media.Photo>
    }

    class PhotoViewHolder
    constructor(
        private val binding: MediaItemContentBinding,
        private val interaction: Interaction?,
        private val dragStartListener: OnStartDragListener?
    ) : RecyclerView.ViewHolder(
        binding.root,
    ) {


        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Media.Photo) = with(binding.root) {
            binding.deleteButton.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item)
            }
            binding.photo = item

            binding.mediaCardView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragStartListener?.onStartDrag(this@PhotoViewHolder)
                }
                false
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Media.Photo)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(photoList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        photoList.removeAt(position)
        notifyItemRemoved(position)
        onSwiped()
    }
}
