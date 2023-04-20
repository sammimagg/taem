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
    private val SPLASH_SCREEN_TIMEOUT: Long = 5000 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sessionUser: SessionUser? = getSessionUser(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if(sessionUser != null) {
                lifecycleScope.launch{
                    Log.d("TESt", sessionUser.accountType)
                    val res = Fetcher().isAuthenticated(sessionUser.accessToken,this@SplashScreenActivity)
                    if(sessionUser.accountType == "0") {
                        val intent = Intent(this@SplashScreenActivity, MainAdminActivity::class.java)
                        startActivity(intent)
                    }
                    else if(sessionUser.accountType == "2") {
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            else {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            }


            finish()
        }, SPLASH_SCREEN_TIMEOUT)
    }
}
