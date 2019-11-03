package ru.hotmule.beaconfinder.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hotmule.beaconfinder.data.db.models.BeaconSync

@Dao
interface BeaconSyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sync: BeaconSync)

    @Query("SELECT * FROM beacon_sync_table WHERE mac = :mac")
    fun getSync(mac: String) : LiveData<BeaconSync>
}