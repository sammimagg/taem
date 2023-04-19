package `is`.hi.hbv601g.taem.Networking
import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate


data class SessionUser(
    var username: String,
    var accessToken: String,
    var accountType: String,
    var ssn: String,
    var email: String
) {
    override fun toString(): String {
        return "SessionUser(username='$username', authToken='$accessToken', " +
                "accountType='$accountType' ssn='$ssn', email='$email')"
    }
}

data class Driving(
    val ssn: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val distance: Double
)

const val PREF_NAME = "MyAppPrefs"
private const val KEY_USERNAME = "username"
private const val KEY_AUTH_TOKEN = "authToken"
private const val KEY_ACCOUNT_TYPE = "accountType"
private const val KEY_SSN = "ssn"
private const val KEY_EMAIL = "email"

fun saveSessionUser(context: Context, sessionUser: SessionUser) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putString(KEY_USERNAME, sessionUser.username)
    editor.putString(KEY_AUTH_TOKEN, sessionUser.accessToken)
    editor.putString(KEY_ACCOUNT_TYPE, sessionUser.accountType)
    editor.putString(KEY_SSN, sessionUser.ssn)
    editor.putString(KEY_EMAIL, sessionUser.email)

    editor.apply()
}

fun getSessionUser(context: Context): SessionUser? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    val username = sharedPreferences.getString(KEY_USERNAME, null)
    val authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    val accountType = sharedPreferences.getString(KEY_ACCOUNT_TYPE, null)
    val ssn = sharedPreferences.getString(KEY_SSN, null)
    val email = sharedPreferences.getString(KEY_EMAIL, null)

    return if (username != null && authToken != null && accountType != null && ssn != null && email != null) {
        SessionUser(username, authToken, accountType, ssn, email)
    } else {
        null
    }
}

fun clearUserData(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Remove specific user-related data
    editor.remove(KEY_AUTH_TOKEN)
    editor.remove(KEY_USERNAME)
    editor.remove(KEY_EMAIL)
    // ... add any other keys you want to remove

    // Apply the changes
    editor.apply()
}