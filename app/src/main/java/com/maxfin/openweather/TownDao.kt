package com.maxfin.openweather

import android.arch.persistence.room.*


@Dao
interface TownDao {

    @Query("SELECT * FROM  Town")
    fun getAll(): List<Town>

    @Insert
    fun insert(town: Town)

    @Delete
    fun delete(town: Town)


}