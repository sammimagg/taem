package `is`.hi.hbv601g.taem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.Networking.getSessionUser
import kotlinx.coroutines.launch

/**
* This activity represents the splash screen displayed at app launch. It checks if the user is already authenticated
* and redirects to the appropriate activity if so, otherwise redirects to the login activity.
* @constructor Creates a new instance of SplashScreenActivity
 */
class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_TIMEOUT: Long = 3000 // 5 seconds

    /**
    * Called when the activity is starting. It sets up the layout and checks if the user is already authenticated.
    * @param savedInstanceState the previously saved state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val sessionUser: SessionUser? = getSessionUser(this)
        if (sessionUser != null) {
            // Launch a new coroutine to make the network call
            lifecycleScope.launch {
                val res = Fetcher().isAuthenticated(sessionUser.accessToken, this@SplashScreenActivity)
                if (sessionUser.accountType == "0") {
                    val intent = Intent(this@SplashScreenActivity, MainAdminActivity::class.java)
                    startActivity(intent)
                } else if (sessionUser.accountType == "2") {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        } else {
            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set a timeout for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, SPLASH_SCREEN_TIMEOUT)
    }
}


