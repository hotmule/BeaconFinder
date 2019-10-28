package ru.hotmule.beaconfinder.data.ble

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import kotlinx.coroutines.*
import org.altbeacon.beacon.*
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import ru.hotmule.beaconfinder.data.BleDevice
import kotlin.math.pow
import kotlin.math.roundToInt


class BleDevicesSource(private val context: Context) : BeaconConsumer {

    interface BeaconsListener {
        fun onBeaconsDiscovered(devices: List<BleDevice>)
    }

    interface NonBeaconListener {
        fun onNonBeaconDiscovered(device: BleDevice)
    }

    private lateinit var beaconsListener: BeaconsListener
    private lateinit var nonBeaconListener: NonBeaconListener
    private lateinit var manager: BeaconManager
    private lateinit var nonBeaconDevicesJob: Job

    override fun getApplicationContext() = context

    override fun bindService(service: Intent?,
                             connection: ServiceConnection?,
                             flags: Int)
            = connection?.let { context.bindService(service, it, flags) } ?: false

    override fun unbindService(connection: ServiceConnection?) {
        connection?.let { context.unbindService(it) }
    }

    override fun onBeaconServiceConnect() {

        manager.removeAllMonitorNotifiers()

        manager.setNonBeaconLeScanCallback { bluetoothDevice, rssi, _ ->

            val nonBeacon = BleDevice(
                rssi = rssi,
                mac = bluetoothDevice.address
            )

            CoroutineScope(Dispatchers.Main + nonBeaconDevicesJob).launch {
                nonBeaconListener.onNonBeaconDiscovered(nonBeacon)
            }
        }

        manager.addRangeNotifier { data, _ ->

            beaconsListener.onBeaconsDiscovered(
                data.map {
                    BleDevice(
                        mac = it.bluetoothAddress,
                        uuid = it.identifiers[0].toString(),
                        major = it.identifiers[1].toInt(),
                        minor = it.identifiers[2].toInt(),
                        rssi = it.rssi,
                        distance = roundToTwoDecimals(it.distance),
                        isBeacon = true
                    )
                }
            )
        }

        try {

            manager.startRangingBeaconsInRegion(
                Region(context.packageName, null, null, null))

        } catch (e: RemoteException) { }
    }

    private fun roundToTwoDecimals(number: Double): Double {
        val factor = 10.0.pow(2.0)
        return (number * factor).roundToInt() / factor
    }

    fun start(beaconsListener: BeaconsListener,
              nonBeaconListener: NonBeaconListener) {

        this.beaconsListener = beaconsListener
        this.nonBeaconListener = nonBeaconListener

        nonBeaconDevicesJob = Job()

        manager = BeaconManager.getInstanceForApplication(context)
        manager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"))

        manager.bind(this)
    }

    fun stop() {
        nonBeaconDevicesJob.cancel()
        manager.unbind(this)
    }
}