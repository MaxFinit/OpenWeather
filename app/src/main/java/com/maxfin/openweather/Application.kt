package com.maxfin.openweather

import android.app.Application
import android.arch.persistence.room.Room


class Application : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(applicationContext, TownDatabase::class.java, "database").allowMainThreadQueries()
            .build()
    }

    companion object {
        lateinit var instance: Application
        lateinit var database: TownDatabase
    }
}