package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boot_records")
data class BootRecord(@PrimaryKey(autoGenerate = true) val id: Int = 0, val bootTime: Long)
/*TODO Use LocalDateTime  with TypeConverter instead of Long just*/
