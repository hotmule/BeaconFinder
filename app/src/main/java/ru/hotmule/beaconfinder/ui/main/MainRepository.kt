package ru.hotmule.beaconfinder.ui.main

import kotlinx.coroutines.*
import ru.hotmule.beaconfinder.data.BleDevice
import ru.hotmule.beaconfinder.data.ble.BleDevicesSource
import ru.hotmule.beaconfinder.data.db.BleDevicesDao

class MainRepository(private val devicesSource: BleDevicesSource,
                     private val devicesDao: BleDevicesDao) {

    private val refreshDeviceJob = Job()
    private val refreshDeviceScope = CoroutineScope(refreshDeviceJob + Dispatchers.IO)

    fun startBleDevicesScanning() {

        devicesSource.start(

            object : BleDevicesSource.BeaconsListener {

                override fun onBeaconsDiscovered(devices: List<BleDevice>) {
                    refreshDeviceScope.launch { devicesDao.insertAll(devices) }
                }
            },

            object : BleDevicesSource.NonBeaconListener {

                override fun onNonBeaconDiscovered(device: BleDevice) {
                    refreshDeviceScope.launch { devicesDao.insert(device) }
                }
            }
        )
    }

    fun stopBleDevicesScanning() {
        //devicesSource.stop()
        refreshDeviceScope.launch { devicesDao.deleteAll() }
    }
}