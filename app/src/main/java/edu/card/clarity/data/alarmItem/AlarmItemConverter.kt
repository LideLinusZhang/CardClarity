package edu.card.clarity.data.alarmItem

fun AlarmItem.toSchedulerAlarmItem(): SchedulerAlarmItem {
    return SchedulerAlarmItem(
        id = this.id,
        time = this.time,
        message = this.message,
        creditCardId = this.creditCardId
    )
}