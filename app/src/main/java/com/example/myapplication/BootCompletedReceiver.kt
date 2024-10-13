package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.data.BootRecord
import com.example.myapplication.data.BootRecordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {

    private val bootRecordDao: BootRecordDao by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                bootRecordDao.insert(BootRecord(bootTime = System.currentTimeMillis()))
            }
        }
    }
}