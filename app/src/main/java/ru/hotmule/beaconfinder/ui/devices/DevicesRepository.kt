package ru.hotmule.beaconfinder.ui.devices

import androidx.lifecycle.liveData
import ru.hotmule.beaconfinder.data.db.dao.DevicesDao

class DevicesRepository(private val devicesDao: DevicesDao) {

    fun getDevices(beaconsOnly: Boolean) = liveData {

        val devices = if (beaconsOnly)
            devicesDao.getBeacons()
        else
            devicesDao.getAllDevices()

        emitSource(devices)
    }
}