package ru.hotmule.beaconfinder.ui.beacon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

class BeaconViewModel(private val repository: BeaconRepository) : ViewModel() {

    lateinit var mac: String

    val beacon by lazy { repository.getBeacon(mac) }
    val beaconSync by lazy { Transformations.switchMap(beacon) { repository.getBeaconSync(it) } }

    private val _rssiData by lazy { MutableLiveData<ArrayList<Float>>() }
    private val _distanceData by lazy { MutableLiveData<ArrayList<Float>>() }

    val rssiData by lazy {
        Transformations.switchMap(beacon) { updateChartData(_rssiData, it.rssi.toFloat()) }
    }

    val distanceData by lazy {
        Transformations.switchMap(beacon) {
            updateChartData(_distanceData, it.distance.toFloat())
        }
    }

    private fun updateChartData(liveData: MutableLiveData<ArrayList<Float>>, item: Float)
            : MutableLiveData<ArrayList<Float>> {

        var data = liveData.value
        if (data == null) data = arrayListOf()
        data.add(0, item)
        return liveData.apply { value = data }
    }
}