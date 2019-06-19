package com.maxfin.openweather

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class Town(
    @PrimaryKey
    var id: String,
    var name: String,
    @Embedded
    var weather: Weather
)
