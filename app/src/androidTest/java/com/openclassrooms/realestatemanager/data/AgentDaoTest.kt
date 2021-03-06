package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@SmallTest
class AgentDaoTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var agentDao: AgentDao

    @Before
    fun initDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        propertyDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        ).build()
        agentDao = propertyDatabase.agentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        propertyDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAllAgentReturnEmptyList() = runBlocking {
        // The list is empty

        //Get first agent in the list
        val firstAgent = agentDao.getAllAgents().first()
        //First agent size should be 0
        assertEquals(0, firstAgent.size)
        closeDb()
    }


    @FlowPreview
    @Test
    @Throws(Exception::class)
    fun insertAndGetAgent() = runBlocking {
        //Create fake agent
        val agent1 = AgentFactory.makeAgent()
        val agent2 = AgentFactory.makeAgent()

        val agent3 = AgentEntity("1", "John Wayne", "kjjk", "121221", "ddd")
        val agent4 = AgentEntity("2", "Clark Gable", "hhhj", "12125421", "")

        //Insert fake agents
        agentDao.insertAgent(agent3)
        agentDao.insertAgent(agent4)

        val fakeAgentList = mutableListOf<AgentEntity>(agent3, agent4)

        val channelFlow = agentDao.getAllAgents().produceIn(this)
        assertEquals(channelFlow.receive(), fakeAgentList)
        assertTrue(channelFlow.isEmpty)
        channelFlow.cancel()

        closeDb()
    }

    @Test
    @Throws(Exception::class)
    fun getAgentById() = runBlocking {
        //Create fake agent
        val agent1 = AgentFactory.makeAgent()
        val agent2 = AgentFactory.makeAgent()

        //Insert fake agents
        agentDao.insertAgent(agent1)
        agentDao.insertAgent(agent2)

        // Get agent with Id of the fake agent1
        val agent1Dao = agentDao.getAgentById(agent1.id).first()
        assertEquals(agent1, agent1Dao)

        // Get agent with Id of the fake agent2
        val agent2Dao = agentDao.getAgentById(agent2.id).first()
        //Fake agent2 should be equal to agent2 of database
        assertEquals(agent2, agent2Dao)
        //Fake agent2 should not be equal to agent1 of database
        assertNotEquals(agent2, agent1Dao)
        closeDb()
    }

}
