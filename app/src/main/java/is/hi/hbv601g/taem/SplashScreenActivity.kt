package `is`.hi.hbv601g.taem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.Networking.getSessionUser
import `is`.hi.hbv601g.taem.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_TIMEOUT: Long = 2000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sessionUser: SessionUser? = getSessionUser(this)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start main activity
            if(sessionUser != null) {
                Log.d("Statuts account type",sessionUser.accountType )
                lifecycleScope.launch{
                    val res = Fetcher().isAuthenticated(sessionUser.accessToken,this@SplashScreenActivity)

                    if(sessionUser.accountType == "0") {
                        val intent = Intent(this@SplashScreenActivity, MainAdminActivity::class.java)
                    }
                    else {
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    }
                }
            }
            else {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            }

            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            // Close splash screen activity
            finish()
        }, SPLASH_SCREEN_TIMEOUT)
    }
}
