package ru.hotmule.beaconfinder.ui.main

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import android.util.Log
import kotlinx.coroutines.*
import org.altbeacon.beacon.*
import org.altbeacon.beacon.BeaconManager
import ru.hotmule.beaconfinder.data.BleDevice
import ru.hotmule.beaconfinder.data.db.BleDevicesDao
import ru.hotmule.beaconfinder.utils.roundToTwoDecimals


class MainRepository(private val context: Context,
                     private val devicesManager: BeaconManager,
                     private val devicesDao: BleDevicesDao) {

    private val refreshDevicesJob = Job()
    private var scope = CoroutineScope(Dispatchers.IO + refreshDevicesJob)

    private var nonBeaconCounter = 0
    private val nonBeaconDelay = 10

    private val beaconConsumer = object : BeaconConsumer {

        override fun bindService(service: Intent?,
                                 connection: ServiceConnection?,
                                 flags: Int)
                = connection?.let { context.bindService(service, it, flags) } ?: false

        override fun unbindService(connection: ServiceConnection?) {
            connection?.let {
                context.unbindService(it)
            }
        }

        override fun onBeaconServiceConnect() {

            devicesManager.removeAllMonitorNotifiers()

            devicesManager.setNonBeaconLeScanCallback { nonBeacon, rssi, _ ->

                if (nonBeaconCounter < nonBeaconDelay)
                    nonBeaconCounter++
                else {
                    nonBeaconCounter = 0
                    scope.launch {
                        devicesDao.insert(
                            BleDevice(
                                rssi = rssi,
                                mac = nonBeacon.address
                            )
                        )
                    }
                }
            }

            devicesManager.addRangeNotifier { beacons, _ ->

                scope.launch {
                    devicesDao.insertAll(beacons.map {
                        BleDevice(
                            mac = it.bluetoothAddress,
                            uuid = it.identifiers[0].toString(),
                            major = it.identifiers[1].toInt(),
                            minor = it.identifiers[2].toInt(),
                            rssi = it.rssi,
                            distance = it.distance.roundToTwoDecimals(),
                            isBeacon = true
                        )
                    })
                }
            }

            try {

                devicesManager.startRangingBeaconsInRegion(
                    Region(context.packageName, null, null, null))

            } catch (e: RemoteException) { }
        }

        override fun getApplicationContext() = context
    }

    fun startBleDevicesScanning() {
        devicesManager.bind(beaconConsumer)
    }

    fun stopBleDevicesScanning() {

        devicesManager.unbind(beaconConsumer)

        scope.launch {
            devicesDao.deleteAll()
        }
    }
}