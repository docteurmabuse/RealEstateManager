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
        val typeDropdown: AutoCompleteTextView = binding.typeDropdown
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
            Timber.tag("FabClick").d("It's ok FAB2")
            val type = binding.typeDropdown.text
            val price = binding.priceTextInput.text.toString().toInt()
            val surface = binding.surfaceTextInput.text.toString().toInt()
            val roomNumber = binding.numberOfRoomTextInput.text.toString().toInt()
            val bathroomNumber = binding.numberOfBathroomTextInput?.text.toString().toInt()
            val bedroomNumber = binding.numberOfBedroomTextInput?.text.toString().toInt()

            //val navHostFragment = findNavController()
            //navHostFragment.navigate(R.id.propertyListFragment)
            //saveProperty()
            Timber.tag("FabClick")
                .d("It's ok FABSAVE: $type, $price, $surface, $roomNumber, $bathroomNumber, $bedroomNumber")

        }
    }

    fun saveProperty() {
        Timber.tag("FabClick").d("It's ok FABSAVE: ${binding.typeDropdown.text}")
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