package com.openclassrooms.realestatemanager.presentation.ui.addAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddAgentFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.notif.NotificationHelper
import com.openclassrooms.realestatemanager.presentation.EventObserver
import com.openclassrooms.realestatemanager.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint

class AddEditAgentFragment : Fragment(R.layout.add_agent_fragment) {
    private var isEditAgentView: Boolean = false
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
        setupNavigation()
        retrievedArguments()
    }

    private fun setupNavigation() {
        viewModelEdit.agentUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            /*val action = AddEditAgentFragmentDirections.actionAddAgentFragmentToItemTabsFragment2()
            findNavController().navigate(action)*/
            val navHostFragment = findNavController()
            navHostFragment.navigate(R.id.mainActivity)
            val agentName: String = binding.agentName.text.toString()
            NotificationHelper.createNotification(
                requireContext(),
                "Agent $agentName was added",
                "",
                "",
                autoCancel = false
            )
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModelEdit.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun retrievedArguments() {
        val bundle = arguments
        if (bundle == null) {
            Timber.d("PropertyDetailFragment did not received arguments")
            return
        }
        //  val args = PropertyDetailFragmentArgs.fromBundle(bundle)
        // agent = args.agent
        // isEditAgentView = args.editAgentView
        // agent?.let { setAgentInLayout(it) }
    }
}
