package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BootRecord::class], version = 1)
abstract class BootRecordsDatabase : RoomDatabase() {

    abstract fun bootRecordDao(): BootRecordDao
}