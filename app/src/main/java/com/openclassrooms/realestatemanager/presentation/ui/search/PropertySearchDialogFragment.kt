package com.openclassrooms.realestatemanager.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchDialogListDialogBinding
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchDialogListDialogItemBinding


// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    PropertySearchDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class PropertySearchDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPropertySearchDialogListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPropertySearchDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //   list.layoutManager = GridLayoutManager(context, 2)
        //    activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
        //   arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }
    }

    private inner class ViewHolder(binding: FragmentPropertySearchDialogListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val text: TextView = binding.text
    }

    private inner class PropertyAdapter(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentPropertySearchDialogListDialogItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): PropertySearchDialogFragment =
            PropertySearchDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
