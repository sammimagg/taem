package `is`.hi.hbv601g.taem.Networking
import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate


import com.google.gson.Gson
import `is`.hi.hbv601g.taem.Persistance.Employee
private const val KEY_EMPLOYEE = "employee"

fun saveLocalUser(context: Context, employee: Employee) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val employeeJson = gson.toJson(employee)
    editor.putString(KEY_EMPLOYEE, employeeJson)
    editor.apply()
}

fun getLocalUser(context: Context): Employee? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val gson = Gson()
    val employeeJson = sharedPreferences.getString(KEY_EMPLOYEE, null)

    return if (employeeJson != null) {
        gson.fromJson(employeeJson, Employee::class.java)
    } else {
        null
    }
}
