package `is`.hi.hbv601g.taem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{ login() }
        val registerButton = findViewById<Button>(R.id.sign_button)
        registerButton.setOnClickListener{ register()}
    }
    private fun login() {
        val fetcher = Fetcher(this )
        var sessionUser = fetcher.AuthenticationRequest("http://10.0.2.2:8080/auth/login", "dori", "123", this )
        println("Test: $sessionUser")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        println("test:  $sessionUser")
    }
    private fun register() {

    }
}