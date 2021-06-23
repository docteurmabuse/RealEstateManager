package com.openclassrooms.realestatemanager.presentation.ui.drawer

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentBottomsheetBinding
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomsheetBinding? = null
    private val binding get() = _binding!!
    private var dialog: BottomSheetDialog? = null
    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog!!.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(sheet)
            behavior!!.isHideable = false
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog as BottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationBottomView.setNavigationItemSelectedListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddAgentFragment()
            val navHostFragment = findNavController()

            when (menuItem.itemId) {

                R.id.navLoan -> navHostFragment.navigate(action)

            }
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true
        }
    }
}
