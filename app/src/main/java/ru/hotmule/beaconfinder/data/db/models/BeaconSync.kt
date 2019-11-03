package ru.hotmule.beaconfinder.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "beacon_sync_table")
data class BeaconSync(@PrimaryKey val mac: String,
                      val date: Date)