package com.openclassrooms.realestatemanager.presentation.ui.search

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>

    private var _binding: FragmentPropertySearchFilterDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun getTheme(): Int {

        return com.openclassrooms.realestatemanager.R.style.CustomBottomSheetDialog
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var cal = Calendar.getInstance()

    @ExperimentalCoroutinesApi
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(sheet)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog

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
        binding.viewModel = viewModel
        binding.fragment = this
        setObserver()
        setDatesListener()
        initRoomsListener()
        initBedsListener()
        initBathsListener()
        initPicsListener()
    }

    fun setExpandableListener(cardView: CardView, button: ImageButton) {
        if (cardView.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(
                cardView,
                AutoTransition()
            )
            cardView.visibility = View.GONE
            button.setImageResource(com.openclassrooms.realestatemanager.R.drawable.ic_baseline_keyboard_arrow_down_24)
        } else {
            TransitionManager.beginDelayedTransition(
                cardView,
                AutoTransition()
            )
            cardView.visibility = View.VISIBLE
            button.setImageResource(com.openclassrooms.realestatemanager.R.drawable.ic_baseline_keyboard_arrow_up_24)
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

    @ExperimentalCoroutinesApi
    private fun initRoomsListener() {
        binding.roomsSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.roomNumber.value =
                binding.roomsSlider.value
            viewModel.filterData()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initBedsListener() {
        binding.bedsSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.bedroomNumber.value =
                binding.bedsSlider.value
            viewModel.filterData()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initBathsListener() {
        binding.bathsSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.bathroomNumber.value =
                binding.bathsSlider.value
            viewModel.filterData()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initPicsListener() {
        binding.picsSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.picsNumber.value =
                binding.picsSlider.value
            viewModel.filterData()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initAreaListener(list: List<Property>) {
        val dropdownAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                list.map { it.address?.area }.toSet().toList()
            )
        binding.filterArea.setAdapter(dropdownAdapter)

        binding.filterArea.setOnItemClickListener { parent, view, position, id ->
            val selectedArea = parent.getItemAtPosition(position) as String
            Timber.d("AGENT_SELECTED: $selectedArea")
            viewModel.area.value = selectedArea
            viewModel.filterData()
        }
    }

    companion object {
        fun newInstance(itemCount: Int): PropertySearchDialogFragment =
            PropertySearchDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }
    }

}
