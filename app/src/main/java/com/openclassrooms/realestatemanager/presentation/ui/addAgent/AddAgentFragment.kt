package com.openclassrooms.realestatemanager.presentation.ui.addAgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R

class AddAgentFragment : Fragment() {

    companion object {
        fun newInstance() = AddAgentFragment()
    }

    private lateinit var viewModel: AddAgentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_agent_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddAgentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
