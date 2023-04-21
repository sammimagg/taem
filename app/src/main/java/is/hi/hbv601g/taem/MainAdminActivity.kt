package `is`.hi.hbv601g.taem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.taem.databinding.ActivityMainAdminBinding

class MainAdminActivity : AppCompatActivity(), OnScanSuccessListener {
    private lateinit var binding: ActivityMainAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ClockInOutFragment(), "clock_in_out_fragment")
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.clockinout -> replaceFragment(ClockInOutFragment(), "clock_in_out_fragment")
                R.id.employees -> replaceFragment(EmployeesFragment(), "employees_fragment")
                R.id.real_time_insights -> replaceFragment(RealTimeIngsightsFragment(), "real_time_insights_fragment")
                R.id.profile -> replaceFragment(ProfileFragment(), "profile_fragment")
                R.id.more -> replaceFragment(MoreFragment(), "more_fragment")
                else -> {}
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag)
        fragmentTransaction.commit()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val clockInOutFragment = supportFragmentManager.findFragmentByTag("clock_in_out_fragment") as? ClockInOutFragment
        clockInOutFragment?.handleNfcIntent(intent)
    }
    override fun onScanSuccess() {
        // Perform the fragment transaction here
        val newFragment = SuccessfulNfcScanFragment();
        replaceFragment(newFragment,"successful_nfc_scan")
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("nfcscann", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}