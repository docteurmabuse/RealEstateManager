package com.openclassrooms.realestatemanager.presentation.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.Event
import com.openclassrooms.realestatemanager.utils.LoanUtils.calculateLoan
import timber.log.Timber

class LoanViewModel : ViewModel() {


    var propertyPrice = MutableLiveData<String>("")
    var downPayment = MutableLiveData<String>("")
    var interestRate = MutableLiveData<String>("1")
    var interest = MutableLiveData<Float>(0F)
    var loanTerm = MutableLiveData<Float>(5F)
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var _monthlyLoan = MutableLiveData<String>("")
    val monthlyLoan: LiveData<String> get() = _monthlyLoan
    fun applyChange() {
        if (propertyPrice.value?.isEmpty() == true && downPayment.value?.isEmpty() == true && interestRate.value?.isEmpty() == true) {
            _snackbarText.value = Event(R.string.empty_loan_message)
            return
        } else {
            _monthlyLoan.value = calculateLoan(
                price = propertyPrice.value!!,
                rate = interestRate.value!!,
                down = downPayment.value!!,
                terms = loanTerm.value!!
            )
        }
        Timber.d("LOAN: ${propertyPrice.value} , ${downPayment.value}, ${interestRate.value}, ${monthlyLoan.value} ")
    }
}
