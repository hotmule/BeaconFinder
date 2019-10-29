package ru.hotmule.beaconfinder.di

import androidx.room.Room
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.hotmule.beaconfinder.data.ble.BleDevicesSource
import ru.hotmule.beaconfinder.data.db.BleDevicesDb
import ru.hotmule.beaconfinder.ui.beacon.BeaconRepository
import ru.hotmule.beaconfinder.ui.beacon.BeaconViewModel
import ru.hotmule.beaconfinder.ui.devices.DevicesRepository
import ru.hotmule.beaconfinder.ui.main.MainRepository
import ru.hotmule.beaconfinder.ui.devices.DevicesViewModel
import ru.hotmule.beaconfinder.ui.main.MainViewModel

val applicationModule = module {

    single { BleDevicesSource(get()) }

    single {
        Room.databaseBuilder(get(), BleDevicesDb::class.java, "ble_devices_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<BleDevicesDb>().devicesDao }

    single { MainRepository(get(), get()) }

    single { DevicesRepository(get()) }

    single { BeaconRepository(get()) }

    viewModel { MainViewModel(get()) }

    viewModel { DevicesViewModel(get()) }

    viewModel { BeaconViewModel(get()) }
}