package edu.card.clarity.data.alarmItem

import edu.card.clarity.SchedulerAlarmItem

fun AlarmItem.toSchedulerAlarmItem(): SchedulerAlarmItem {
    return SchedulerAlarmItem(
        id = this.id,
        time = this.time,
        message = this.message,
        creditCardId = this.creditCardId
    )
}