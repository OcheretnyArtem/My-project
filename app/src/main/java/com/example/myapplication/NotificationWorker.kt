// NotificationWorker.kt
package com.example.bootcounter

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.data.BootRecordDao
import com.example.myapplication.format
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

    @SuppressLint("MissingPermission")
    private suspend fun showNotification() {
        val channelId = "special_channel"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "App Boots Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notificationBody = notificationBody()
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("App Boots")
            .setContentText(notificationBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        /*TODO do permission check before */
        notificationManagerCompat.notify(notificationId, notification)
    }

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
}