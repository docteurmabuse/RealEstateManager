package com.openclassrooms.realestatemanager.presentation

//import leakcanary.AppWatcher
import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.notif.NotificationHelper
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

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel."
        )
    }
}
