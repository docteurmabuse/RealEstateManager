package com.openclassrooms.realestatemanager.presentation.ui.search

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchFilterDialogBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import com.openclassrooms.realestatemanager.utils.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*


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

    private var cal = Calendar.getInstance()

    private val dateSellSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateSellDateInView()
        }

    @ExperimentalCoroutinesApi
    private val dateSoldSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateSoldDateInView()
        }

    @ExperimentalCoroutinesApi
    private fun updateSellDateInView() {
        val dateOnMarket = cal.timeInMillis
        binding.viewModel?.sellDate = MutableLiveData(dateOnMarket.toString())
        binding.sellDateDropdown.setText(DateUtil.longDateToString(dateOnMarket))
        viewModel.sellDate = MutableLiveData(dateOnMarket.toString())
        viewModel.filterData()
        Timber.d("DATE_PICKER : ${dateOnMarket}")
    }

    @ExperimentalCoroutinesApi
    private fun updateSoldDateInView() {
        val dateOnMarket = cal.timeInMillis
        binding.viewModel?.soldDate = MutableLiveData(dateOnMarket.toString())
        binding.filterSoldDate.setText(DateUtil.longDateToString(dateOnMarket))
        viewModel.soldDate = MutableLiveData(dateOnMarket.toString())
        viewModel.filterData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPropertySearchFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = mainViewModel
        setObserver()
        setDatesListener()
        binding.applyFilter.setOnClickListener {
            mainViewModel.filterData()
            Timber.d("Click")
        }
    }

    @ExperimentalCoroutinesApi
    private fun setDatesListener() {
        val todayDate = cal.timeInMillis

        binding.sellDateDropdown.setOnClickListener {
            val sellDatePicker = DatePickerDialog(
                requireContext(),
                dateSellSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            sellDatePicker.datePicker.maxDate = todayDate
            sellDatePicker.show()


        }

        binding.filterSoldDate.setOnClickListener {
            val soldDatePicker = DatePickerDialog(
                requireContext(),
                dateSoldSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            soldDatePicker.datePicker.maxDate = todayDate
            soldDatePicker.show()
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


    @ExperimentalCoroutinesApi
    private fun initSurfaceListener(list: List<Property>) {
        binding.surfaceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.surfaceArray.value =
                arrayOf(binding.surfaceRangeSlider.values[0], binding.surfaceRangeSlider.values[1])
            viewModel.filterData()
            Timber.d(" SURFACE_RANGE: ${viewModel.surfaceArray.value}")
        }
    }

    @ExperimentalCoroutinesApi
    private fun initPriceListener(list: List<Property>) {
        binding.priceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.priceArray.value =
                arrayOf(binding.priceRangeSlider.values[0], binding.priceRangeSlider.values[1])
            viewModel.filterData()
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
