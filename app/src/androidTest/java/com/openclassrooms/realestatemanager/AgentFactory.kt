package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object AgentFactory {
    fun makeAgent(): AgentEntity {
        return AgentEntity(
            makeRandomInt(),
            makeRandomString(),
            makeRandomString()
        )
    }

    private fun makeRandomString() = UUID.randomUUID().toString()

    private fun makeRandomInt() =
        ThreadLocalRandom.current().nextInt(0, 1000 + 1)
}