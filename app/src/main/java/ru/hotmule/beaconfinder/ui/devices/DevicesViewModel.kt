package ru.hotmule.beaconfinder.ui.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.hotmule.beaconfinder.data.db.models.BleDevice

class DevicesViewModel(private val repository: DevicesRepository) : ViewModel() {

    val beaconsOnly = MutableLiveData<Boolean>(false)

    val devices: LiveData<List<BleDevice>>
            = Transformations.switchMap(beaconsOnly) { repository.getDevices(it) }

    val clickedDeviceMac = MutableLiveData<String>()
    val clickedDeviceIsNotBeacon = MutableLiveData<Boolean>()

    fun onDeviceClick(mac: String, isBeacon: Boolean) {

        if (isBeacon)
            clickedDeviceMac.value = mac
        else
            clickedDeviceIsNotBeacon.value = true
    }

    fun onDeviceNavigated() {
        clickedDeviceMac.value = null
    }

    fun onIsNotBeaconMessageShown() {
        clickedDeviceIsNotBeacon.value = null
    }
}