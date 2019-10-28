package ru.hotmule.beaconfinder.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    var permissionGranted = false

    fun permissionGranted() {
        if (!permissionGranted) {
            permissionGranted = true
            repository.startBleDevicesScanning()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopBleDevicesScanning()
    }
}