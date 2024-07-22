package edu.card.clarity

interface AlarmScheduler {
    fun schedule(item: SchedulerAlarmItem)
    fun cancel(item: SchedulerAlarmItem)
}