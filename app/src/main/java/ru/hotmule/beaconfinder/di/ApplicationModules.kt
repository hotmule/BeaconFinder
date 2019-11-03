package ru.hotmule.beaconfinder.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.hotmule.beaconfinder.data.api.BleDevicesApi
import ru.hotmule.beaconfinder.data.ble.BleDevicesSource
import ru.hotmule.beaconfinder.ui.main.MainRepository
import ru.hotmule.beaconfinder.data.db.BleDevicesDb
import ru.hotmule.beaconfinder.ui.beacon.BeaconRepository
import ru.hotmule.beaconfinder.ui.beacon.BeaconViewModel
import ru.hotmule.beaconfinder.ui.devices.DevicesRepository
import ru.hotmule.beaconfinder.ui.devices.DevicesViewModel
import ru.hotmule.beaconfinder.ui.main.MainViewModel

val applicationModule = module {

    single {
        BeaconManager.getInstanceForApplication(androidContext()).apply {
            foregroundBetweenScanPeriod = 2500
            beaconParsers.add(
                BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"))
        }
    }

    single {
        Room.databaseBuilder(androidContext(), BleDevicesDb::class.java, "ble_devices_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://hotmulebeaconfinder.herokuapp.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()
                )
            )
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .build()
            )
            .build()
    }

    single { get<BleDevicesDb>().devicesDao }

    single { get<BleDevicesDb>().beaconSyncDao }

    single { get<Retrofit>().create(BleDevicesApi::class.java) }

    single { BleDevicesSource(androidContext(), get()) }

    single { MainRepository(get(), get()) }

    single { DevicesRepository(get()) }

    single { BeaconRepository(get(), get(), get()) }

    viewModel { MainViewModel(get()) }

    viewModel { DevicesViewModel(get()) }

    viewModel { BeaconViewModel(get()) }
}