package com.openclassrooms.realestatemanager.data

import com.github.javafaker.Faker
import com.openclassrooms.realestatemanager.db.model.property.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object PropertyFactory {

    private const val propertyId: String = "1"
    private val faker: Faker = Faker()
    private val makeRandomPhotoList =
        listOf(makeRandomPhoto(), makeRandomPhoto(), makeRandomPhoto())

    private val makeRandomVideoList: List<VideoEntity> =
        listOf(makeRandomVideo(), makeRandomVideo(), makeRandomVideo())

    private val agentId = "1"
    fun makeProperty(): PropertyEntityAggregate {
        return PropertyEntityAggregate(
            makeRandomProperty(),
            makeRandomPhotoList,
            makeRandomVideoList,
            makeRandomAddress()
        )
    }

    fun makeOneProperty(): PropertyEntityAggregate {
        return PropertyEntityAggregate(
            property,
            makeRandomPhotoList,
            makeRandomVideoList,
            makeRandomAddress()
        )
    }

    val property = PropertyEntity(
        propertyId,
        type = "Manor",
        price = 450000,
        surface = 250,
        roomNumber = 5,
        bathroomNumber = 6,
        bedroomNumber = 6,
        description = "Nice manor except ghost at night",
        schools = true,
        shops = true,
        park = false,
        stations = true,
        hospital = false,
        museum = false,
        sold = false,
        sellDate = null,
        soldDate = null,
        agentId
    )

    private fun makeRandomProperty(): PropertyEntity {
        return PropertyEntity(
            propertyId,
            makeRandomString(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomString(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomLong(),
            makeRandomLong(),
            agentId,
        )
    }

    private fun makeRandomPhoto(): PhotoEntity {
        return PhotoEntity(
            makeRandomLong(),
            propertyId,
            makeRandomString(),
            makeRandomString(),
        )
    }

    private fun makeRandomVideo(): VideoEntity {
        return VideoEntity(
            makeRandomLong(),
            propertyId,
            makeRandomString(),
            makeRandomString(),
        )
    }

    private fun makeRandomAddress(): AddressEntity {
        return AddressEntity(
            makeRandomLong(),
            propertyId,
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomDouble(),
            makeRandomDouble(),
        )
    }

    private fun makeRandomString() = UUID.randomUUID().toString()

    private fun makeRandomInt() =
        ThreadLocalRandom.current().nextInt(0, 1000 + 1)

    private fun makeRandomDouble() =
        ThreadLocalRandom.current().nextDouble(0.0, 1000.0 + 1.0)

    private fun makeRandomLong() =
        ThreadLocalRandom.current().nextLong(1619155158, 1619155158 + 1)

    private fun makeRandomBoolean() =
        ThreadLocalRandom.current().nextBoolean()
}
