package ru.hotmule.beaconfinder.ui.main

import kotlinx.coroutines.*
import ru.hotmule.beaconfinder.data.BleDevice
import ru.hotmule.beaconfinder.data.ble.BleDevicesSource
import ru.hotmule.beaconfinder.data.db.BleDevicesDao

class MainRepository(private val devicesSource: BleDevicesSource,
                     private val devicesDao: BleDevicesDao) {

    private val refreshDeviceJob = Job()
    private val refreshDeviceScope = CoroutineScope(refreshDeviceJob + Dispatchers.Main)

    fun startBleDevicesScanning() {

        devicesSource.start(

            object : BleDevicesSource.BeaconsListener {

                override fun onBeaconsDiscovered(devices: List<BleDevice>) {
                    refreshDeviceScope.launch { refreshDevices(devices) }
                }
            },

            object : BleDevicesSource.NonBeaconListener {

                override fun onNonBeaconDiscovered(device: BleDevice) {
                    refreshDeviceScope.launch { refreshDevice(device) }
                }
            }
        )
    }

    suspend fun refreshDevices(devices: List<BleDevice>) {
        withContext(Dispatchers.IO) {
            devicesDao.insertAll(devices)
        }
    }

    suspend fun refreshDevice(device: BleDevice) {
        withContext(Dispatchers.IO) {
            devicesDao.insert(device)
        }
    }

    suspend fun clearDevices() {
        withContext(Dispatchers.IO) {
            devicesDao.deleteAll()
        }
    }

    fun stopBleDevicesScanning() {
        refreshDeviceScope.launch { clearDevices() }
    }
}