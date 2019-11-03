package ru.hotmule.beaconfinder.ui.main

import kotlinx.coroutines.*
import ru.hotmule.beaconfinder.data.ble.BleDevicesSource
import ru.hotmule.beaconfinder.data.db.models.BleDevice
import ru.hotmule.beaconfinder.data.db.dao.DevicesDao


class MainRepository(private val devicesSource: BleDevicesSource,
                     private val devicesDao: DevicesDao) {

    val scope = CoroutineScope(Dispatchers.IO)

    fun startBleDevicesScanning() {

        devicesSource.start(object : BleDevicesSource.DevicesListener {

            override fun onBeaconsDiscovered(beacons: List<BleDevice>) {
                scope.launch { devicesDao.insertAll(beacons) }
            }

            override fun onNonBeaconDiscovered(nonBeacon: BleDevice) {
                scope.launch { devicesDao.insert(nonBeacon) }
            }
        })
    }

    fun stopBleDevicesScanning() {
        devicesSource.stop()
        scope.launch { devicesDao.deleteAll() }
    }
}