package `is`.hi.hbv601g.taem

import DrivingLogFragment
import android.content.ContentValues
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import `is`.hi.hbv601g.taem.Networking.*
import `is`.hi.hbv601g.taem.Storage.db
import `is`.hi.hbv601g.taem.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentAdmin = Intent(this, MainAdminActivity::class.java)
        val intentUser = Intent(this, MainActivity::class.java)
        val sessionUser: SessionUser? = getSessionUser(this)

        if (sessionUser != null && !sessionUser.accessToken.isNullOrEmpty() && sessionUser.accountType == "0") {
            startActivity(intentAdmin)
        }
        else if(sessionUser != null && !sessionUser.accessToken.isNullOrEmpty() && sessionUser.accountType == "2") {
            startActivity(intentUser)
        }
        else {
            val binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val loginButton = findViewById<Button>(R.id.login_button)
            loginButton.setOnClickListener{ loginButtonHandler() }
            val registerButton = findViewById<Button>(R.id.signUp_botton)
            registerButton.paintFlags = registerButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            registerButton.setOnClickListener{ register()}
        }
    }
    private suspend fun login(user: String, password: String): Pair<SessionUser?, Int> {
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val fetcher = Fetcher( )
        val (sessionUser, responseCode) = fetcher.AuthenticationRequest( user, password, this )

        return Pair(sessionUser, responseCode)
    }
    fun loginButtonHandler() {
        val user = findViewById<EditText>(R.id.username_field).text.toString()
        val password = findViewById<EditText>(R.id.password_field).text.toString()
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val intentAdmin = Intent(this, MainAdminActivity::class.java)
        val intentUser = Intent(this, MainActivity::class.java)
        if(user.isEmpty()) {
            errorMessage.text = "Username can't be empty"
        }
        if(password.isEmpty()) {
            errorMessage.text = ("Password can't be empty")
        }
        lifecycleScope.launch {
            val (sessionUser, responseCode) = async { login(user, password) }.await()
            if(responseCode == 401) { // 401 Unauthorized
                errorMessage.text = ("Wrong password or username")
            }
            else if(sessionUser != null) {
                clearLocalUserFromSharedPreferences(this@LoginActivity);
                clearUserData(this@LoginActivity)
                saveSessionUser(this@LoginActivity, sessionUser);
                val employee = Fetcher().fetchEmployeeProfile(sessionUser.ssn,this@LoginActivity)
                saveLocalUserInSharedPreferences(this@LoginActivity,employee);

                if(sessionUser.accountType == "0") {
                    startActivity(intentAdmin) // Admin notandi
                }
                else if(sessionUser.accountType == "2"){
                    startActivity(intentUser) // Venjulegur notandi
                }
            }
        }
    }
    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}