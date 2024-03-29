package ru.hotmule.beaconfinder.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hotmule.beaconfinder.data.db.models.Beacon
import ru.hotmule.beaconfinder.data.db.models.BleDevice

@Dao
interface DevicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: BleDevice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<BleDevice>)

    @Query("DELETE FROM ble_devices_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM ble_devices_table ORDER BY mac")
    fun getAllDevices() : LiveData<List<BleDevice>>

    @Query("SELECT * FROM ble_devices_table WHERE isBeacon = 1 ORDER BY distance")
    fun getBeacons() : LiveData<List<BleDevice>>

    @Query("SELECT * FROM ble_devices_table WHERE mac = :mac")
    fun getBeacon(mac: String) : LiveData<BleDevice>
}