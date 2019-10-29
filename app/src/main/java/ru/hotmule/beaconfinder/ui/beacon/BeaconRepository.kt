package ru.hotmule.beaconfinder.ui.beacon

import androidx.lifecycle.liveData
import ru.hotmule.beaconfinder.data.db.BleDevicesDao

class BeaconRepository(private val deviceDao: BleDevicesDao) {

    fun getBeacon(mac: String) = liveData {
        val beacon = deviceDao.getBeacon(mac)
        emitSource(beacon)
    }
}