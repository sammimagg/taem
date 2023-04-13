package `is`.hi.hbv601g.taem

import DrivingLogFragment
import android.content.ContentValues
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.Storage.db
import `is`.hi.hbv601g.taem.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{ loginButtonHandler() }
        val registerButton = findViewById<Button>(R.id.signUp_botton)
        registerButton.paintFlags = registerButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        registerButton.setOnClickListener{ register()}
    }
    private suspend fun login(user: String, password: String): Pair<SessionUser?, Int> {
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val fetcher = Fetcher( )
        val (sessionUser, responseCode) = fetcher.AuthenticationRequest("https://hbv501g-group-8-production.up.railway.app/auth/login", user, password, this )
        // Dóri hvað viltu gera við sessionUser ?
        // Ég vil nota það í að vita hvaða user er að sækja t.d. transactions
        return Pair(sessionUser, responseCode)
    }
    fun loginButtonHandler() {
        val user = findViewById<EditText>(R.id.username_field).text.toString()
        val password = findViewById<EditText>(R.id.password_field).text.toString()
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val intent = Intent(this, MainActivity::class.java)
        val intentAdmin = Intent(this, MainAdminActivity::class.java)
        val dbHelper = db.SessionUserContract.DBHelper(this)
        if(user.isEmpty()) {
            errorMessage.text = "Username cant be empty"
        }
        if(password.isEmpty()) {
            errorMessage.text = ("Password can't be empty")
        }
        lifecycleScope.launch {
            val (sessionUser, responseCode) = async { login(user, password) }.await()
            if(responseCode == 401) { // 401 Unauthorized
                errorMessage.text = ("Wrong password or username")
            }
            else {
                // TODO Gera pláss f. AccountType í SessionUser
                val db = dbHelper.writableDatabase
      

                val values = ContentValues().apply {
                    put(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.COLUMN_NAME_USERNAME,
                        sessionUser?.username)
                    put(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.COLUMN_NAME_AUTH_TOKEN,
                        sessionUser?.accessToken)
                    put(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.COLUMN_NAME_ACCOUNT_TYPE,
                        sessionUser?.accountType)
                    put(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.COLUMN_NAME_SSN,
                        sessionUser?.ssn)
                }

                val newRowId = db?.insert(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.TABLE_NAME,
                                            null, values)

                val db2 = dbHelper.readableDatabase
                val cursor = db2.query(
                    `is`.hi.hbv601g.taem.Storage.db.SessionUserContract.
                    SessionUserEntry.TABLE_NAME,   // The table to query
                    arrayOf(`is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.COLUMN_NAME_SSN),             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null
                )

                with(cursor) {
                    moveToLast()
                    println(cursor.getString(0))
                }

                //startActivity(intent) // Venjulegur notandi
                startActivity(intentAdmin) // Admin notandi
            }
        }
    }
    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun drivinglog() {
        val intent = Intent(this, DrivingLogFragment::class.java)
        startActivity(intent)
    }
}