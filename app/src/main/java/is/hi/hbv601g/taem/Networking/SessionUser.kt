package `is`.hi.hbv601g.taem.Networking

data class SessionUser(
    var username: String,
    var accessToken: String,
    var ssn : String,

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


