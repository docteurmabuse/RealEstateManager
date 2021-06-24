package com.openclassrooms.realestatemanager.utils

import java.text.NumberFormat
import java.util.*

object LoanUtils {
    fun calculateLoanInDollar(price: String, rate: String, down: String, terms: Float): String {
        var result: String = ""
        val rateFloat = rate.toDoubleOrNull()?.div(100) ?: 0.0
        val priceFloat = price.toDoubleOrNull() ?: 0.0
        val downFloat = down.toDoubleOrNull() ?: 0.0
        val interest = (rateFloat * (priceFloat - downFloat) * terms)
        val result2 = (interest + priceFloat - downFloat) / (12 * terms)
        result = NumberFormat.getCurrencyInstance(Locale.US).format(result2).toString()
        return result
    }

    fun calculateInterestInDollar(price: String, rate: String, down: String, terms: Float): String {
        var result: String = ""
        val rateFloat = rate.toDoubleOrNull()?.div(100) ?: 0.0
        val priceFloat = price.toDoubleOrNull() ?: 0.0
        val downFloat = down.toDoubleOrNull() ?: 0.0
        val interest = (rateFloat * (priceFloat - downFloat) * terms)
        result = NumberFormat.getCurrencyInstance(Locale.US).format(interest).toString()
        return result
    }
}
