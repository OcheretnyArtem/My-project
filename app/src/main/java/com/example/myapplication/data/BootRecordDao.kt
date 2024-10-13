package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BootRecordDao {

    @Insert
    suspend fun insert(bootRecord: BootRecord)

    @Query("SELECT * FROM boot_records")
    fun getAllBootRecords(): Flow<List<BootRecord>>
}
