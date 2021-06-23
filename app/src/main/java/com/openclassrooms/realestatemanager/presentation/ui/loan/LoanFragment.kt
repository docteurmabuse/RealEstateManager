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
    private var _binding: LoanFragmentBinding? = null
    private val binding get() = _binding!!
    private var monthlyLoan: Float = 0F
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
    }

    private fun setObserver() {
        viewModel.monthlyLoan.observe(viewLifecycleOwner) {
            binding.viewModel = viewModel
            binding.resultText.visibility = View.VISIBLE
            if (it.isNotEmpty())
                this.monthlyLoan = it.toFloat()
        }
    }

    private fun initBathsListener() {
        binding.loanTermSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.loanTerm.value =
                binding.loanTermSlider.value
            viewModel.applyChange()
        }
    }
}
