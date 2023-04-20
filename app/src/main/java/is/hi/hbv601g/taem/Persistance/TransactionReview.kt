package `is`.hi.hbv601g.taem.Persistance

data class TransactionReview(
    val status: String,
    val changedClockIn: String,
    val changedClockOut: String,
    val originalClockIn: String,
    val originalClockOut: String,
    val clockInDate: String,
    val originalClockInTime: String,
    val originalClockOutTime: String,
    val changedClockInTime: String,
    val changedClockOutTime: String,
    val id: Int,
    val ssn: String,
)

data class MappedRequestUserDAO (
    val transactionReview: TransactionReview,
    val employee: Employee
)