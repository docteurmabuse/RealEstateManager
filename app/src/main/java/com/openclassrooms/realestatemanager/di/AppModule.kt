package com.openclassrooms.realestatemanager.di

import android.content.Context
import com.openclassrooms.realestatemanager.presentation.BaseApplication
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchersProvider
import com.openclassrooms.realestatemanager.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider):
            DispatchersProvider

    companion object {
        @Singleton
        @Provides
        fun provideApplication(@ApplicationContext app: Context): BaseApplication {
            return app as BaseApplication
        }
    }
}