package ru.hotmule.beaconfinder

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hotmule.beaconfinder.data.db.models.BleDevice
import ru.hotmule.beaconfinder.data.db.dao.DevicesDao
import ru.hotmule.beaconfinder.data.db.BleDevicesDb
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DevicesDaoTest {

    private lateinit var devicesDao: DevicesDao
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

    /*

    @Test
    @Throws(Exception::class)
    fun insertAndGetDevice() {

        val mac = "mac"
        val device = BleDevice(mac = mac, rssi = 0, isBeacon = false)

        devicesDao.insert(device)
        val insertedDevice = devicesDao.getBeacon(mac)

        assertEquals(insertedDevice.mac, mac)
    }

     */
}

