package com.example.myapplication.di

import androidx.room.Room
import com.example.myapplication.data.BootRecordsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), BootRecordsDatabase::class.java, "boot-records-database").build()
    }
    single { get<BootRecordsDatabase>().userDao() }
}
