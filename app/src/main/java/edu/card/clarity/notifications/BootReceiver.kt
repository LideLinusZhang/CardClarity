package edu.card.clarity.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import edu.card.clarity.data.alarmItem.AlarmItemDao
import edu.card.clarity.data.converters.toSchedulerAlarmItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmItemDao: AlarmItemDao

    @Inject
    lateinit var alarmScheduler: AndroidAlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context ?: return

            CoroutineScope(Dispatchers.IO).launch {
                val allAlarms = alarmItemDao.getAllAlarms()

                allAlarms.forEach { alarmItem ->
                    Log.d("boot", alarmItem.message)
                    alarmScheduler.schedule(alarmItem.toSchedulerAlarmItem())
                }
            }
        }
    }
}
