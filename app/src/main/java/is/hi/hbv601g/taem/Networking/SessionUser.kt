package `is`.hi.hbv601g.taem.Networking

import java.time.LocalDate

data class SessionUser(
    var username: String,
    var accessToken: String,
    var ssn : String,
    var email : String,

    ) {
    override fun toString(): String {
        return "SessionUser(username='$username', authToken='$accessToken')"
    }


    /**
     * @TODO
     * Refactor to /Persistance/
     * add SSN to variables
     */

}
data class Driving(
    val ssn: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val distance: Double
)


