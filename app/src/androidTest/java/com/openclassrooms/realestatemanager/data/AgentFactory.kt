package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object AgentFactory {
    fun makeAgent(): AgentEntity {
        return AgentEntity(
            "Marlon Brando",
            makeRandomString(),
            makeRandomString(),
            makeRandomString()
        )
    }

    private fun makeRandomLong() =
        ThreadLocalRandom.current().nextLong(0, 1000 + 1)

    private fun makeRandomString() = UUID.randomUUID().toString()

}