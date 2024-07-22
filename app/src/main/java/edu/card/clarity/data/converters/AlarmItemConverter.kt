package edu.card.clarity.data.converters

import edu.card.clarity.data.alarmItem.AlarmItem
import edu.card.clarity.notifications.SchedulerAlarmItem

fun AlarmItem.toSchedulerAlarmItem(): SchedulerAlarmItem {
    return SchedulerAlarmItem(
        id = this.id,
        time = this.time,
        message = this.message,
        creditCardId = this.creditCardId
    )
}