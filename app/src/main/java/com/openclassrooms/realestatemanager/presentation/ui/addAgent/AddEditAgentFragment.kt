package com.openclassrooms.realestatemanager.presentation.ui.addAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddAgentFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class AddEditAgentFragment : Fragment(R.layout.add_agent_fragment) {
    private val viewModelEdit: AddEditAgentViewModel by viewModels()
    private var _binding: AddAgentFragmentBinding? = null
    private val binding get() = _binding!!
    private var agent: Agent? = null

    companion object {
        fun newInstance() = AddEditAgentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddAgentFragmentBinding.inflate(inflater, container, false)
        // Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModelEdit
        binding.agent = agent
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModelEdit.snackbarText, Snackbar.LENGTH_SHORT)
    }
}