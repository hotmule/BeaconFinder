package ru.hotmule.beaconfinder

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hotmule.beaconfinder.data.BleDevice
import ru.hotmule.beaconfinder.data.db.BleDevicesDao
import ru.hotmule.beaconfinder.data.db.BleDevicesDb
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BleDevicesDaoTest {

    private lateinit var devicesDao: BleDevicesDao
    private lateinit var db: BleDevicesDb

    @Before
    fun createDb() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, BleDevicesDb::class.java)
            .allowMainThreadQueries()
            .build()

        devicesDao = db.devicesDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetDevice() {

        val mac = "mac"
        val device = BleDevice(mac = mac)

        devicesDao.insert(device)
        val insertedDevice = devicesDao.getBeacon(mac)

        assertEquals(insertedDevice.mac, mac)
    }
}

