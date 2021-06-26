package com.openclassrooms.realestatemanager.presentation.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.LoanFragmentBinding
import com.openclassrooms.realestatemanager.utils.setupSnackbar

class LoanFragment : Fragment(R.layout.loan_fragment) {

    companion object {
        fun newInstance() = LoanFragment()
    }

    private val viewModel: LoanViewModel by viewModels()
    private var _binding: LoanFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoanFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initBathsListener()
        setObserver()
        setupSnackbar()
    }

    private fun setObserver() {
        viewModel.monthlyLoan.observe(viewLifecycleOwner) {
            binding.viewModel = viewModel

            binding.resultText.visibility = View.VISIBLE
        }
    }

    private fun initBathsListener() {
        binding.loanTermSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.loanTerm.value =
                binding.loanTermSlider.value
            viewModel.applyChange()
        }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}
