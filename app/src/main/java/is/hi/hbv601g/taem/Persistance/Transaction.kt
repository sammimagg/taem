package `is`.hi.hbv601g.taem.Persistance

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.sql.Time
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(
    val status: String,
    val clockIn: String,
    val clockOut: String?,
    val finished: Boolean,
    val clockInDate: String?,
    val clockInTime: String?,
    val clockOutTime: String?,
    val workedTime: String?,
    val workedHours: Int,
    val workedMinutes: Int,
    val duration: Double?,
    val id: Long,
    val ssn: String
) {
    override fun toString(): String {
        return "Transaction(status='$status', clockIn=$clockIn, clockOut=$clockOut, finished=$finished, clockInDate=$clockInDate, clockInTime=$clockInTime, clockOutTime=$clockOutTime, workedTime=$workedTime, workedHours=$workedHours, workedMinutes=$workedMinutes, duration=$duration, id=$id, ssn='$ssn')"
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    }

    fun getClockInLocalDateTime(): LocalDateTime {
        return LocalDateTime.parse(clockIn, formatter)
    }

    fun getClockOutLocalDateTime() : LocalDateTime {
        return LocalDateTime.parse(clockOut, formatter)
    }

}
