package ru.hotmule.beaconfinder.data.api

import androidx.lifecycle.LiveData
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import ru.hotmule.beaconfinder.data.BleDevice

interface BleDevicesApi {

    @POST("/beacons/new")
    fun addBeacon(beacon: BleDevice) : LiveData<ResponseBody>
}