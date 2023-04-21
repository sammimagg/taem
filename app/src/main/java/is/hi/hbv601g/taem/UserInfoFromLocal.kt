import android.content.Context
import android.provider.BaseColumns
import androidx.core.content.ContentProviderCompat.requireContext
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import `is`.hi.hbv601g.taem.Storage.db.SessionUserContract

suspend fun fetchEmployeeInfo(context: Context): Employee = withContext(Dispatchers.IO) {
    val db2 = SessionUserContract.DBHelper(context).readableDatabase
    val cursor = db2.query(
        SessionUserContract.SessionUserEntry.TABLE_NAME,
        null,
        null,
        null,
        null,
        null,
        BaseColumns._ID
    )
    with(cursor) {
        moveToLast()
    }
    val ssnToUse = cursor.getString(4)
    return@withContext runBlocking {
        async {
            getEmployeeInformation(
                ssnToUse,
                context
            )
        }.await()
    }
}
fun getSSNFromLocalStorage(context: Context):String {
    val db2 = SessionUserContract.DBHelper(context).readableDatabase
    val cursor = db2.query(
        SessionUserContract.SessionUserEntry.TABLE_NAME,
        null,
        null,
        null,
        null,
        null,
        BaseColumns._ID
    )
    with(cursor) {
        moveToLast()
    }
    val ssnToUse = cursor.getString(4)
    return ssnToUse
}

private suspend fun getEmployeeInformation( ssn: String,context: Context) : Employee {
    val fetcher = Fetcher();
    val response = fetcher.fetchEmployeeProfile(ssn,context);
    return response
}


