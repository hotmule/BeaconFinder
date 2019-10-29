package ru.hotmule.beaconfinder.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private var _permissionGranted = false

    fun permissionGranted() {
        if (!_permissionGranted) {
            _permissionGranted = true
            repository.startBleDevicesScanning()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopBleDevicesScanning()
    }
}