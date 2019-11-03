package ru.hotmule.beaconfinder.ui.beacon

import androidx.lifecycle.liveData
import ru.hotmule.beaconfinder.data.api.BleDevicesApi
import ru.hotmule.beaconfinder.data.db.dao.BeaconSyncDao
import ru.hotmule.beaconfinder.data.db.dao.DevicesDao
import ru.hotmule.beaconfinder.data.db.models.BleDevice
import java.lang.Exception

class BeaconRepository(private val devicesDao: DevicesDao,
                       private val devicesApi: BleDevicesApi,
                       private val beaconSyncDao: BeaconSyncDao) {

    fun getBeacon(mac: String) = liveData {
        val beacon = devicesDao.getBeacon(mac)
        emitSource(beacon)
    }

    fun getBeaconSync(beacon: BleDevice) = liveData {

        try {

            val remoteSync = devicesApi.addBeacon(beacon)

            beaconSyncDao.insert(remoteSync)
            val localSync = beaconSyncDao.getSync(beacon.mac)

            emitSource(localSync)

        } catch (e: Exception) { }
    }
}