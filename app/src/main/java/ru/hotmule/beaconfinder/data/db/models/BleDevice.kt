package ru.hotmule.beaconfinder.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "ble_devices_table")
data class BleDevice(@Expose @PrimaryKey var mac: String = "",
                     @Expose var uuid: String = "",
                     @Expose var major: Int = 0,
                     @Expose var minor: Int = 0,
                     @Expose var rssi: Int = 0,
                     @Expose var distance: Double = 0.0,
                     var isBeacon: Boolean = false)