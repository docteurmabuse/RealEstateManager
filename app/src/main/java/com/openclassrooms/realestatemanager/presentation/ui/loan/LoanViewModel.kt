package com.openclassrooms.realestatemanager.presentation.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import kotlin.math.roundToInt

class LoanViewModel : ViewModel() {


    var propertyPrice = MutableLiveData<String>("")
    var downPayment = MutableLiveData<String>("")
    var interestRate = MutableLiveData<String>("1")
    var interest = MutableLiveData<Float>(0F)
    var loanTerm = MutableLiveData<Float>(5F)
    private var _monthlyLoan = MutableLiveData<String>("")
    val monthlyLoan: LiveData<String> get() = _monthlyLoan
    fun applyChange() {
        if (propertyPrice.value?.isNotEmpty() == true && downPayment.value?.isNotEmpty() == true && interestRate.value?.isNotEmpty() == true) {
            interest.value =
                (interestRate.value!!.toFloat() / 100 * propertyPrice.value!!.toFloat()) * loanTerm.value!!
            _monthlyLoan.value =
                ((interest.value!! + propertyPrice.value!!.toFloat() / 12).toString())
        }
        interest.value =
            (interestRate.value!!.toFloat() / 100 * propertyPrice.value!!.toFloat()) * loanTerm.value!!
        _monthlyLoan.value =
            ((interest.value!! + propertyPrice.value!!.toFloat()) / (12.0 * loanTerm.value!!)).roundToInt()
                .toString()
        Timber.d("LOAN: ${propertyPrice.value} , ${downPayment.value}, ${interestRate.value}, ${monthlyLoan.value} ")
    }
}
