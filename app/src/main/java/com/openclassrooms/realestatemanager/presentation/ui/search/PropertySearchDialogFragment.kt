package com.openclassrooms.realestatemanager.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchFilterDialogBinding
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"


@AndroidEntryPoint
class PropertySearchDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPropertySearchFilterDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding.lifecycleOwner = requireActivity()
        _binding = FragmentPropertySearchFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //   list.layoutManager = GridLayoutManager(context, 2)
        //    activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
        //   arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it)
        binding.viewModel = mainViewModel
        binding.applyFilter.setOnClickListener {
            mainViewModel.filterData()
            Timber.d("Click")
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
