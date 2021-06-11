package com.openclassrooms.realestatemanager.presentation.ui.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchFilterDialogBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
        //  binding.lifecycleOwner = requireActivity()
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
        lifecycleScope.launchWhenStarted {
            val value = viewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {

                        it.data?.let { properties -> renderList(properties) }
                    }
                    DataState.Status.LOADING -> {
                    }
                    DataState.Status.ERROR -> {
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
        viewModel.properties.observe(requireActivity()) {
            renderList(it)
        }
    }

    @ExperimentalCoroutinesApi
    private fun renderList(list: List<Property>) {
        initPriceListener(list)
        initSurfaceListener(list)
        initAreaListener(list)
    }


    private fun initSurfaceListener(list: List<Property>) {
        binding.surfaceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.surfaceArray.value =
                arrayOf(binding.surfaceRangeSlider.values[0], binding.surfaceRangeSlider.values[1])
            viewModel.filterData()
            Timber.d(" SURFACE_RANGE: ${viewModel.surfaceArray.value}")
        }
    }

    private fun initPriceListener(list: List<Property>) {
        Timber.d(" PRICE_RANGE: ${viewModel.minPrice.value}, ${viewModel.maxPrice.value}}")
        binding.priceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.priceArray.value =
                arrayOf(binding.priceRangeSlider.values[0], binding.priceRangeSlider.values[1])
            viewModel.filterData()
            Timber.d(" SURFACE_RANGE: ${viewModel.priceArray.value}")
        }
    }

    private fun initAreaListener(list: List<Property>) {
        val dropdownAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                list.map { it.address?.area }.toSet().toList()
            )
        binding.filterArea.setAdapter(dropdownAdapter)
    }

    companion object {
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
