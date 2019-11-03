package ru.hotmule.beaconfinder.data.ble

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region
import ru.hotmule.beaconfinder.data.db.models.BleDevice
import ru.hotmule.beaconfinder.utils.roundToTwoDecimals

class BleDevicesSource(private val context: Context,
                       private val devicesManager: BeaconManager) : BeaconConsumer {

    interface DevicesListener {

        fun onBeaconsDiscovered(beacons: List<BleDevice>)

        fun onNonBeaconDiscovered(nonBeacon: BleDevice)
    }

    lateinit var listener: DevicesListener

    fun start(listener: DevicesListener) {
        this.listener = listener
        devicesManager.bind(this)
    }

    fun stop() {
        devicesManager.apply {
            unbind(this@BleDevicesSource)
            removeAllRangeNotifiers()
            removeAllMonitorNotifiers()
            nonBeaconLeScanCallback = null
        }
    }

    private var nonBeaconCounter = 0
    private val nonBeaconDelay = 50

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
                listener.onNonBeaconDiscovered(
                    BleDevice(
                        rssi = rssi,
                        mac = nonBeacon.address
                    )
                )
            }
        }

        devicesManager.addRangeNotifier { beacons, _ ->
            listener.onBeaconsDiscovered(
                beacons.map {
                    BleDevice(
                        mac = it.bluetoothAddress,
                        uuid = it.identifiers[0].toString(),
                        major = it.identifiers[1].toInt(),
                        minor = it.identifiers[2].toInt(),
                        rssi = it.rssi,
                        distance = it.distance.roundToTwoDecimals(),
                        isBeacon = true
                    )
                }
            )
        }

        try {
            devicesManager.startRangingBeaconsInRegion(
                Region(context.packageName, null, null, null))
        } catch (e: RemoteException) { }
    }

    override fun getApplicationContext() = context
}