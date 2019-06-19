package com.maxfin.openweather

import java.util.*

class TownManager {

    private val database: TownDao =
        Application.database.townDao()

    fun addTown(townName: String, weather: Weather) {
        val town = Town(UUID.randomUUID().toString(), townName, weather)
        database.insert(town)
    }


    fun deleteTown(town: Town) {
        database.delete(town)
    }

    fun getAllTown(): List<Town> {
        return database.getAll()
    }


}