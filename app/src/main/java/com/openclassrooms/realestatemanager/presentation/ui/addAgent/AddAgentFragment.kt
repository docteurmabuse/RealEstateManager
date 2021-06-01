package com.openclassrooms.realestatemanager.presentation.ui.addAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddAgentFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class AddAgentFragment : Fragment(R.layout.add_agent_fragment) {
    private val viewModel: AddAgentViewModel by viewModels()
    private var _binding: AddAgentFragmentBinding? = null
    private val binding get() = _binding!!
    private var agent: Agent? = null

    companion object {
        fun newInstance() = AddAgentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddAgentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.agent = agent
    }
}
