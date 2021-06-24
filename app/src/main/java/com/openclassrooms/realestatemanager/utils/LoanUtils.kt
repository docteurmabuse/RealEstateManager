package com.openclassrooms.realestatemanager.utils

object LoanUtils {
    fun calculateLoan(price: String, rate: String, down: String, terms: Float): String {
        var result: String = ""
        val rateFloat = rate.toFloatOrNull()?.div(100) ?: 0F
        val priceFloat = price.toFloatOrNull() ?: 0F
        val downFloat = down.toFloatOrNull() ?: 0F
        val interest = (rateFloat * (priceFloat - downFloat) * terms)
        result =
            String.format("%.2f", (interest + priceFloat - downFloat) / (12 * terms))
        return result
    }
}
