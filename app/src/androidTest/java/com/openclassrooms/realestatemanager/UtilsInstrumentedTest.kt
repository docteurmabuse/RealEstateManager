package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.openclassrooms.realestatemanager.utils.Utils.isNetworkConnected
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.lang.Thread.sleep

class UtilsInstrumentedTest {

    private val testApp = UiDevice.getInstance(getInstrumentation())


    @Test
    @Throws(Exception::class)
    fun testIsInternetAvailable() {
        enableAirplaneMode()
        val context = ApplicationProvider.getApplicationContext<Context>()
        sleep(3000)
        isNetworkConnected(context)
        assertTrue(isNetworkConnected(context))
    }


    private fun enableAirplaneMode() = apply {
        testApp.openQuickSettings()
        testApp.waitForIdle()

        val airplaneModeIcon =
            checkNotNull(testApp.findObject(By.desc("Airplane,mode,Off.,Button")))

        airplaneModeIcon.click()
    }
}
