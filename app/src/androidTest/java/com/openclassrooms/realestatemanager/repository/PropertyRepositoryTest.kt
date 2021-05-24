package com.openclassrooms.realestatemanager.repository

import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest

class PropertyRepositoryTest {

   /* private val fakerServer = FakeServer()
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
    }*/
}