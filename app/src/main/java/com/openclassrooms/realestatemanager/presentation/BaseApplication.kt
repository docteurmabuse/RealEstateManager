package com.openclassrooms.realestatemanager.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import leakcanary.AppWatcher

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false)
    }

}