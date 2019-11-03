package ru.hotmule.beaconfinder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.hotmule.beaconfinder.data.db.converters.DateConverter
import ru.hotmule.beaconfinder.data.db.dao.BeaconSyncDao
import ru.hotmule.beaconfinder.data.db.dao.DevicesDao
import ru.hotmule.beaconfinder.data.db.models.BeaconSync
import ru.hotmule.beaconfinder.data.db.models.BleDevice

@Database(entities = [BleDevice::class, BeaconSync::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class BleDevicesDb : RoomDatabase() {

    abstract val devicesDao: DevicesDao

    abstract val beaconSyncDao: BeaconSyncDao
}