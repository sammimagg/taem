package `is`.hi.hbv601g.taem.Persistance

import java.sql.Time
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Transaction(
    val status: String,
    val clockIn: LocalDateTime,
    val clockOut: LocalDateTime,
    val finished: Boolean,
    val clockInDate: LocalDate?,
    val clockInTime: LocalTime?,
    val clockOutTime: LocalTime?,
    val workedTime: Time?,
    val workedHours: Int,
    val workedMinutes: Int,
    val duration: Duration,
    val id: Long,
    val ssn: String
)
