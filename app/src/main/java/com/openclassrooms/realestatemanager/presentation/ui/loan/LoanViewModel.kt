package com.openclassrooms.realestatemanager.presentation.ui.loan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoanViewModel : ViewModel() {
    var propertyPrice = MutableLiveData<String>("")
    var downPayment = MutableLiveData<String>("")
    var interestRate = MutableLiveData<String>("")


}
