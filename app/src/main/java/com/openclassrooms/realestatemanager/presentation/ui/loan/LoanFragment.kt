package com.openclassrooms.realestatemanager.presentation.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.LoanFragmentBinding

class LoanFragment : Fragment(R.layout.loan_fragment) {

    companion object {
        fun newInstance() = LoanFragment()
    }

    private val viewModel: LoanViewModel by viewModels()
    private lateinit var binding: LoanFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoanFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}
