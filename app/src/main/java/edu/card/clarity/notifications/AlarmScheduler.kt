package edu.card.clarity.notifications

interface AlarmScheduler {
    fun schedule(item: SchedulerAlarmItem)
    fun cancel(item: SchedulerAlarmItem)
}