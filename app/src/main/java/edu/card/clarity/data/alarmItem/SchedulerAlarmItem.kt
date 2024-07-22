package edu.card.clarity.data.alarmItem

import java.time.LocalDateTime
import java.util.UUID

class SchedulerAlarmItem (
    val id: UUID,
    val time: LocalDateTime,
    val message: String,
    val creditCardId: UUID
)