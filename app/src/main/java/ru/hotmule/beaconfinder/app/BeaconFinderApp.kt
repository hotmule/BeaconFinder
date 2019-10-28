package ru.hotmule.beaconfinder.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.hotmule.beaconfinder.di.applicationModule

class BeaconFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BeaconFinderApp)
            modules(applicationModule)
        }
    }
}