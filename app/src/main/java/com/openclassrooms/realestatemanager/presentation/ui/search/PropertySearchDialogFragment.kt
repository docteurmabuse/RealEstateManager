package com.openclassrooms.realestatemanager.presentation.ui.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchFilterDialogBinding
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber


// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"


@AndroidEntryPoint
class PropertySearchDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPropertySearchFilterDialogBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //   list.layoutManager = GridLayoutManager(context, 2)
        //    activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
        //   arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it)
        binding.viewModel = mainViewModel
        setObserver()
        binding.applyFilter.setOnClickListener {
            mainViewModel.filterData()
            Timber.d("Click")
        }
    }

    @ExperimentalCoroutinesApi
    private fun setObserver() {
        viewModel.properties.observe(requireActivity()) {
            renderList(it)
        }
    }

    private fun renderList(list: List<Property>) {
        val minPrice = list.minWithOrNull(Comparator.comparingInt { it.price!! })?.price?.toFloat()
        val maxPrice = list.maxWithOrNull(Comparator.comparingInt { it.price!! })?.price?.toFloat()
        viewModel.minPrice.value = minPrice
        viewModel.maxPrice.value = maxPrice
        viewModel.priceArray.value = arrayOf(minPrice!!, maxPrice!!)

        val minSurface =
            list.minWithOrNull(Comparator.comparingInt { it.surface!! })?.surface?.toFloat()
        val maxSurface =
            list.maxWithOrNull(Comparator.comparingInt { it.surface!! })?.surface?.toFloat()
        viewModel.minSurface.value = minSurface
        viewModel.maxSurface.value = maxSurface
        viewModel.surfaceArray.value = arrayOf(minSurface!!, maxSurface!!)
        val dropdownAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                list.map { it.address?.area }.toSet().toList()
            )
        binding.filterArea.setAdapter(dropdownAdapter)
        Timber.d(" PRICE_RANGE: ${viewModel.minPrice.value}, ${viewModel.maxPrice.value}}")

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
