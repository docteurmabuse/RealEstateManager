package com.openclassrooms.realestatemanager.utils

import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

object FabUtils {
    fun showFab(fab: FloatingActionButton, isVisible: Boolean) {
        val layoutParams: ViewGroup.LayoutParams = fab.layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            val behavior = layoutParams.behavior
            if (behavior is HideBottomViewOnScrollBehavior) {
                if (isVisible) {
                    behavior.slideUp(fab)
                } else {
                    behavior.slideDown(fab)
                }
            }
        }
    }
}
