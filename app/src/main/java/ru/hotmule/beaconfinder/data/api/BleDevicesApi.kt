package ru.hotmule.beaconfinder.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.hotmule.beaconfinder.data.db.models.BeaconSync
import ru.hotmule.beaconfinder.data.db.models.BleDevice
import java.util.*

interface BleDevicesApi {

    @POST("/beacons/new")
    suspend fun addBeacon(@Body beacon: BleDevice) : BeaconSync
}