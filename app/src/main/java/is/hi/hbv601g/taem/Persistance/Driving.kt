package `is`.hi.hbv601g.taem.Persistance

data class Driving(
    val licencePlate : String?,
    val odometerStart: Int,
    val odometerEnd: Int,
    val dags: String,
    val distanceDriven: Double,
    val id : Int,
    val ssn: String,
)