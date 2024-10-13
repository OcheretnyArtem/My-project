package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.data.BootRecordDao
import kotlinx.coroutines.flow.last
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val notificationManagerCompat: NotificationManagerCompat by inject()
    private val bootEventDao: BootRecordDao by inject()

    override suspend fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private suspend fun showNotification() {
        createNotificationChannel()
        if (notificationManagerCompat.areNotificationsEnabled()) {
            notificationManagerCompat.notify(NOTIFICATION_ID, createNotification())
        } else {
            Log.w("NotificationWorker", "Notifications are not enabled")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "App Boots Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManagerCompat.createNotificationChannel(channel)
        }
    }

    private suspend fun createNotification() = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("App Boots")
        .setContentText(notificationBody())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    private suspend fun notificationBody(): String {
        val bootEvents = bootEventDao.getAllBootRecords().last()
        return when {
            bootEvents.isEmpty() -> "No boots detected"
            bootEvents.size == 1 -> "The boot was detected = ${bootEvents.first().bootTime.format()}"
            else -> {
                val lastBoot = bootEvents[bootEvents.size - 1].bootTime
                val preLastBoot = bootEvents[bootEvents.size - 2].bootTime
                val timeDelta = lastBoot - preLastBoot
                "Last boots time delta = ${timeDelta / 1000} seconds"
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "app_boots_notification_channel"
        private const val NOTIFICATION_ID = 1
    }
}