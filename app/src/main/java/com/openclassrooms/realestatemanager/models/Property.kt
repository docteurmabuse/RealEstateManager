package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Property(
        val id: Int,
        val type: String,
        val price: Int,
        val surface: Int,
        val roomNumber: Int,
        val description: String,
        val addressId: Int,
        val schools: Boolean,
        val shops: Boolean,
        val parcs: Boolean,
        val stations: Boolean,
        val hospital: Boolean,
        val sold: Boolean,
        val entryDate: Date,
        val saleDate: Date,
        val agentId: Int
) : Parcelable
