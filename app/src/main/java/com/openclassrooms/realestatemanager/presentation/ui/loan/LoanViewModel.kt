package com.openclassrooms.realestatemanager.presentation.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.Event
import com.openclassrooms.realestatemanager.utils.LoanUtils.calculateInterestInDollar
import com.openclassrooms.realestatemanager.utils.LoanUtils.calculateLoanInDollar

class LoanViewModel : ViewModel() {


    val propertyPrice = MutableLiveData<String>("")
    val downPayment = MutableLiveData<String>("")
    val interestRate = MutableLiveData<String>("1")
    val interest = MutableLiveData<Float>(0F)
    val interestInDollar = MutableLiveData<String>("")
    val loanTerm = MutableLiveData<Float>(5F)
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _monthlyLoan = MutableLiveData<String>("")
    val monthlyLoan: LiveData<String> get() = _monthlyLoan
    fun applyChange() {
        if (propertyPrice.value?.isEmpty() == true && downPayment.value?.isEmpty() == true && interestRate.value?.isEmpty() == true) {
            _snackbarText.value = Event(R.string.empty_loan_message)
            return
        } else {
            interestInDollar.value = calculateInterestInDollar(
                price = propertyPrice.value!!,
                rate = interestRate.value!!,
                down = downPayment.value!!,
                terms = loanTerm.value!!
            )
            _monthlyLoan.value = calculateLoanInDollar(
                price = propertyPrice.value!!,
                rate = interestRate.value!!,
                down = downPayment.value!!,
                terms = loanTerm.value!!
            )
        }
    }
}
