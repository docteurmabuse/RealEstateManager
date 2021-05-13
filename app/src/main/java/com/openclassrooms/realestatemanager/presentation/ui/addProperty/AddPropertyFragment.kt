package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {
    private val viewModel: AddPropertyViewModel by viewModels()

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private var _binding: AddPropertyFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPropertyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown
        setupMenuValues(typeDropdown)
        //  this.binding.handlers = Handlers()
        binding.lifecycleOwner = this
        this.binding.viewModel = viewModel
        setFabListener()
    }

    private fun setupMenuValues(dropdown: AutoCompleteTextView) {
        val items = Property.PropertyType.values()
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.setAdapter(adapter)
    }

    private fun setFabListener() {
        binding.addPropertyFAB.setOnClickListener {
            setNewPropertyValues()
            //val navHostFragment = findNavController()
            //navHostFragment.navigate(R.id.propertyListFragment)
            //saveProperty()


        }
    }

    private fun setNewPropertyValues() {

        val type = Property.PropertyType.valueOf(binding.type!!.typeDropdown.toString())
        val price = binding.characteristics!!.priceTextInput.text.toString().toInt()
        val surface = binding.characteristics!!.surfaceTextInput.text.toString().toInt()
        val roomNumber = binding.characteristics!!.numberOfRoomTextInput.text.toString().toInt()
        val bathroomNumber =
            binding.characteristics!!.numberOfBathroomTextInput.text.toString().toInt()
        val bedroomNumber =
            binding.characteristics!!.numberOfBedroomTextInput.text.toString().toInt()
        val address = binding.address!!.addressTextInput.text.toString()
        val address2 = binding.address!!.address2TextInput.text.toString()
        val city = binding.address!!.cityTextInput.text.toString()
        val state = binding.address!!.stateTextInput.text.toString()
        val zipcode = binding.address!!.zipcodeTextInput.text.toString().toInt()
        val country = binding.address!!.countryTextInput.text.toString()
        val museum = binding.pointOfInterest!!.museum.isChecked
        val schools = binding.pointOfInterest!!.schools.isChecked
        val shops = binding.pointOfInterest!!.shops.isChecked
        val hospital = binding.pointOfInterest!!.hospital.isChecked
        val station = binding.pointOfInterest!!.station.isChecked
        val parcs = binding.pointOfInterest!!.parcs.isChecked

        Timber.tag("FabClick")
            .d("It's ok FABSAVE: $type, $price, $surface, $roomNumber, $bathroomNumber, $bedroomNumber")
    }

    fun saveProperty() {
        Timber.tag("FabClick").d("It's ok FABSAVE: ${binding.typeDropdown!!.text}")
        //addPropertyViewModel.addPropertyToRoomDb()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Handlers {
        fun onClickFriend(property: String) {
            Timber.tag("FabClick").d("It's ok FAB Handler:$property")
            //private val viewModel: AddPropertyViewModel by viewModels()

        }
    }


}