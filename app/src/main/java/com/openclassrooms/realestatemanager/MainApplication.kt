package com.openclassrooms.realestatemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import leakcanary.AppWatcher

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false)
    }
}