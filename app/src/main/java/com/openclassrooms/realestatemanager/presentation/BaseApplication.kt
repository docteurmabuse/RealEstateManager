package com.openclassrooms.realestatemanager.presentation

import android.app.Application
import com.openclassrooms.realestatemanager.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import leakcanary.AppWatcher
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}