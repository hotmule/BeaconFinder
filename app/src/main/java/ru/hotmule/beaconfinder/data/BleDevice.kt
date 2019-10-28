package ru.hotmule.beaconfinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ble_devices_table")
data class BleDevice(@PrimaryKey var mac: String = "",
                     var uuid: String = "",
                     var major: Int = 0,
                     var minor: Int = 0,
                     var rssi: Int = 0,
                     var distance: Double = 0.0,
                     var isBeacon: Boolean = false)