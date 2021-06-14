package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.IOException

internal class AddEditPropertyFragmentTest : TestCase() {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var propertyDao: PropertyDao
    private val agent =
        AgentEntity("1", "John Wayne", "john45@gmail.com", "121221", "myphotourl.com")


    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initDb() {
        propertyDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        ).build()
        propertyDao = propertyDatabase.propertyDao()
        agentDao = propertyDatabase.agentDao()

        runBlocking {
            //Insert fake agents before the Property for Foreign Key purpose
            agentDao.insertAgent(agent)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        propertyDatabase.close()
    }

}
