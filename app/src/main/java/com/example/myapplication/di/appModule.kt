package com.example.myapplication.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.WorkerParameters
import com.example.bootcounter.NotificationWorker
import com.example.myapplication.data.BootRecordsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), BootRecordsDatabase::class.java, "boot-records-database").build()
    }
    single { NotificationManagerCompat.from(get()) }
    single { get<BootRecordsDatabase>().bootRecordDao() }
    factory { (appContext: Context, workerParams: WorkerParameters) -> NotificationWorker(appContext, workerParams) }
}
