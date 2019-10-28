package ru.hotmule.beaconfinder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.hotmule.beaconfinder.data.BleDevice

@Database(entities = [BleDevice::class], version = 1, exportSchema = false)
abstract class BleDevicesDb : RoomDatabase() {

    abstract val devicesDao: BleDevicesDao
}