package edu.card.clarity.data.alarmItem

interface AlarmScheduler {
    fun schedule(item: SchedulerAlarmItem)
    fun cancel(item: SchedulerAlarmItem)
}