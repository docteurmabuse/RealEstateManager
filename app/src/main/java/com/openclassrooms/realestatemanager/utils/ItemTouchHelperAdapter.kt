package com.openclassrooms.realestatemanager.utils

interface ItemTouchHelperAdapter {
    //Called when item is moved
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    //Called when item is swiped
    fun onItemDismiss(position: Int)

}