package `is`.hi.hbv601g.taem.Persistance

import java.util.*

data class EmployeeRTI(
    val username: String,
    val password: String?,
    val accountType: Int,
    val employeeUserName: String?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String,
    val jobTitle: String,
    val salary: Double,
    val phoneNumber: String,
    val startDate: String,
    val vacationDaysUsed: Int,
    val sickDaysUsed: Int,
    val remainingVacationDays: Int,
    val remainingSickDays: Int,
    val firstNameOfEmployee: String?,
    val lastNameOfEmployee: String?,
    val phoneNumberEmployee: String?,
    val ssnEmployee: String?,
    val status: String?,
    val ssn: String,
    val accounttype: Int,
    // Add mutable properties for clockInTime and clockIn
    var clockInTime: String? = null,
    var clockIn: Boolean = false,
    var last_transaction : Transaction? = null

                    ) {



}
