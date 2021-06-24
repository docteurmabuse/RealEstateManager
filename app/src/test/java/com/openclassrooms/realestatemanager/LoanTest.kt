package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.LoanUtils.calculateLoanInDollar
import junit.framework.TestCase
import org.junit.Test

class LoanTest : TestCase() {
    @Test
    @Throws(Exception::class)
    fun testLoanResultIsZeroIfDownPaymentIsEqualToPrice() {
        //Loan is equal to 100
        val loan = "100"
        val down = "100"
        val rate = "0"
        val term = 5F
        assertEquals("$0.00", calculateLoanInDollar(loan, rate, down, term))
    }

    fun testLoanResultDownIsEqualToZero() {
        //Loan is equal to 100
        val loan = "10000"
        val down = "0"
        val rate = "0"
        val term = 5F
        val result = "$166.67"
        assertEquals(result, calculateLoanInDollar(loan, rate, down, term))
    }

    fun testLoanResultRateIsEqualToOne() {
        //Loan is equal to 100
        val loan = "10000"
        val down = "0"
        val rate = "1"
        val term = 5F
        val result = "$175.00"
        assertEquals(result, calculateLoanInDollar(loan, rate, down, term))
    }

    fun testLoanResultWithAllValues() {
        //Loan is equal to 100
        val loan = "10000"
        val down = "1000"
        val rate = "1"
        val term = 5F
        val result = "$157.50"
        assertEquals(result, calculateLoanInDollar(loan, rate, down, term))
    }
}
