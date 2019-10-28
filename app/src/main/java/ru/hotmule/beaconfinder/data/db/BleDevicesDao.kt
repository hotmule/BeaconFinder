package ru.hotmule.beaconfinder.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hotmule.beaconfinder.data.BleDevice

@Dao
interface BleDevicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: BleDevice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(devices: List<BleDevice>)

    @Query("SELECT * FROM ble_devices_table ORDER BY mac")
    fun getAllDevices() : LiveData<List<BleDevice>>

    @Query("SELECT * FROM ble_devices_table WHERE isBeacon = 1 ORDER BY distance")
    fun getBeacons() : LiveData<List<BleDevice>>

    @Query("SELECT * FROM ble_devices_table WHERE mac = :mac")
    fun getBeacon(mac: String) : LiveData<BleDevice>

    @Query("DELETE FROM ble_devices_table")
    fun deleteAll()
}