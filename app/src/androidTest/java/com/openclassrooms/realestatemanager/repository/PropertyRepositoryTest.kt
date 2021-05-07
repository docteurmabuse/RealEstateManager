package com.openclassrooms.realestatemanager.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.RoomPersistence
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.utils.DispatchersProvider
import com.openclassrooms.realestatemanager.utils.FakeServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
class PropertyRepositoryTest {

    private val fakerServer = FakeServer()
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var persistence: Persistence
    private lateinit var dispatchersProvider: DispatchersProvider

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: PropertyDatabase

    @Module
    @InstallIn(SingletonComponent::class)
    object TestPersistenceModule {
        @Provides
        fun provideRoomDatabase(): PropertyDatabase {
            return Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                PropertyDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }
    }

    @Before
    fun setup() {
        //fakerServer.start()

        hiltRule.inject()

        persistence = RoomPersistence(database.propertyDao(), database.agentDao())

        propertyRepository = PropertyRepository_Impl(
            persistence,
            dispatchersProvider
        )
    }

    @After
    fun tearDown() {
        //fakerServer.shutDown()
    }
}