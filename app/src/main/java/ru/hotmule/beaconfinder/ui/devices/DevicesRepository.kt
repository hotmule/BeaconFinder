package ru.hotmule.beaconfinder.ui.devices

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.hotmule.beaconfinder.data.db.BleDevicesDao

class DevicesRepository(private val devicesDao: BleDevicesDao) {

    fun getDevices(beaconsOnly: Boolean) = liveData(Dispatchers.IO) {

        val devices = if (beaconsOnly)
            devicesDao.getBeacons()
        else
            devicesDao.getAllDevices()

        emitSource(devices)
    }
}