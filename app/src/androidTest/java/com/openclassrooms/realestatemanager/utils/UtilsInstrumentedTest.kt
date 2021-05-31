package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.utils.GeocodeUtils.getLatLngFromAddress
import com.openclassrooms.realestatemanager.utils.Utils.isNetworkConnected
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@HiltAndroidTest
class UtilsInstrumentedTest {

    private val testApp = UiDevice.getInstance(getInstrumentation())

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    @Throws(Exception::class)
    fun testIsInternetAvailableAsNetworkIsDisable() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        getInstrumentation().uiAutomation.executeShellCommand("svc data disable")
        sleep(3000)
        assertThat(isNetworkConnected(context), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun testIsInternetAvailableAsNetworkIsEnable() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
        sleep(3000)
        assertThat(isNetworkConnected(context), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun testIsLatLngFromAddressIsWorking() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val address = "10 E 70th St, New York, NY 10021, United States"
        val location = LatLng(40.7705461, -73.9670704)
        val geocodeLocation = getLatLngFromAddress(address, context)
        Assert.assertEquals(location, geocodeLocation)
    }
}
