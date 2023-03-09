package `is`.hi.hbv601g.taem

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
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
    private suspend fun login(user: String, password: String): Int {
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val fetcher = Fetcher(this )
        val (sessionUser, responseCode) = fetcher.AuthenticationRequest("https://hbv501g-group-8-production.up.railway.app/auth/login", user, password, this )
        // Dóri hvað viltu gera við sessionUser ?
        // Ég vil nota það í að vita hvaða user er að sækja t.d. transactions
        return responseCode
    }
    fun loginButtonHandler() {
        val user = findViewById<EditText>(R.id.username_field).text.toString()
        val password = findViewById<EditText>(R.id.password_field).text.toString()
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val intent = Intent(this, MainActivity::class.java)
        val intentAdmin = Intent(this, MainAdminActivity::class.java)
        if(user.isEmpty()) {
            errorMessage.text = "Username cant be empty"
        }
        if(password.isEmpty()) {
            errorMessage.text = ("Password can't be empty")
        }
        lifecycleScope.launch {
            val responde = async { login(user, password) }.await()
            if(responde == 401) { // 401 Unauthorized
                errorMessage.text = ("Wrong password or username")
            }
            else {
                //startActivity(intent) // Venjulegur notandi
                startActivity(intentAdmin) // Admin notandi
            }
        }
    }
    private fun register() {

    }
}