package `is`.hi.hbv601g.taem

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.Storage.db
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var ssnEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById<EditText?>(R.id.username_field)
        passwordEditText = findViewById(R.id.password_field)
        emailEditText = findViewById(R.id.email_field)
        ssnEditText = findViewById(R.id.ssn_field)
        registerButton = findViewById(R.id.login_button)
        registerButton.setOnClickListener{ registerButtonHandler() }
    }
    private suspend fun register(user: String, password: String, email: String, SSN: String): Boolean {
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val fetcher = Fetcher()
        val success = fetcher.registerRequest(
            user,
            password,
            email,
            SSN,
            this
        )
        return success
    }
    fun registerButtonHandler() {
        val user = findViewById<EditText>(R.id.username_field).text.toString()
        val password = findViewById<EditText>(R.id.password_field).text.toString()
        val email = findViewById<EditText>(R.id.email_field).text.toString()
        val ssn = findViewById<EditText>(R.id.ssn_field).text.toString()
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val intent = Intent(this, LoginActivity::class.java)
        val intentAdmin = Intent(this, LoginActivity::class.java)
        val dbHelper = db.SessionUserContract.DBHelper(this)
        if (user.isEmpty()) {
            errorMessage.text = "Username can not be empty"
        }
        if (password.isEmpty()) {
            errorMessage.text = ("Password can not be empty")
        }
        if (email.isEmpty()) {
            errorMessage.text = ("Email can not be empty")
        }
        if (ssn.isEmpty()) {
            errorMessage.text = ("SSN can not be empty")
        }
        lifecycleScope.launch {
            val success = async { register(user, password, email, ssn) }.await()
            if (success) {
                errorMessage.text = "Registration successful"
                startActivity(intentAdmin)
            } else {
                errorMessage.text = "Registration failed"
            }

            }
        }
    }