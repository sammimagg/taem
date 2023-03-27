package `is`.hi.hbv601g.taem.Persistance

import java.util.*

data class EmployeeRTI(
    var clockInTime: String?,
    var clockOutTime: String?,
    var clockIn: Boolean,
    var workedHours: String,
    val username: String,
    val password: String,
    val accountType: Number,
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String,
    val jobTitle: String,
    val salary: Number,
    val phoneNumber: Number,
    val startDate: Date,
    val vacationDaysUsed: Number,
    val sickDaysUsed: Number,
    val remainingVacationDays: Number,
    val remainingSickDays: Number,
    val status: Number,
    val ssn: Number

                    ) {



}
