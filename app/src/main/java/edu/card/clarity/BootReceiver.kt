package edu.card.clarity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import edu.card.clarity.data.alarmItem.AlarmItemDao
import edu.card.clarity.data.alarmItem.toSchedulerAlarmItem
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

            // Using a coroutine scope to perform database operations
            CoroutineScope(Dispatchers.IO).launch {
                // Fetch all alarm items
                val allAlarms = alarmItemDao.getAllAlarms()

                // Reschedule each alarm
                allAlarms.forEach { alarmItem ->
                    alarmScheduler.schedule(alarmItem.toSchedulerAlarmItem())
                }
            }
        }
    }
}
