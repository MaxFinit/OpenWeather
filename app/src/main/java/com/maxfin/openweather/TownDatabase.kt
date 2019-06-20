package com.maxfin.openweather

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = [Town::class], version = 1,exportSchema = false)
abstract class TownDatabase : RoomDatabase() {
    abstract fun townDao(): TownDao
}


