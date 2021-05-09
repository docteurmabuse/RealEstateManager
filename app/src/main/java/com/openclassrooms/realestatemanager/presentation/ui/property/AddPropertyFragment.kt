package com.openclassrooms.realestatemanager.presentation.ui.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import timber.log.Timber

class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private lateinit var viewModel: AddPropertyViewModel
    private var _binding: AddPropertyFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddPropertyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddPropertyViewModel::class.java)
        setFabListener()
    }

    private fun setFabListener() {
        binding.addPropertyFAB.setOnClickListener {
            Timber.tag("FabClick").d("It's ok FAB")
            val navHostFragment = findNavController()
            navHostFragment.navigate(R.id.propertyListFragment)
            // saveProperty()
        }
    }

    private fun saveProperty() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}