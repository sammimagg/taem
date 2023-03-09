package `is`.hi.hbv601g.taem.Networking

data class SessionUser(
    var username: String,
    var accessToken: String,


    ) {
    override fun toString(): String {
        return "SessionUser(username='$username', authToken='$accessToken')"
    }


}


