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

/**
*This class represents the MainAdminActivity which is the main activity for the admin user.
*It extends the AppCompatActivity class and implements the OnScanSuccessListener interface.
 */
class MainAdminActivity : AppCompatActivity(), OnScanSuccessListener {
    private lateinit var binding: ActivityMainAdminBinding

    /**
    This method is called when the activity is first created. It initializes the layout,
    sets up the bottom navigation, and initializes the ClockInOutFragment.
    @param savedInstanceState The saved instance state bundle
     */
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
    /**
    * This method is used to replace the current fragment with the specified fragment.
    * @param fragment The fragment to replace the current fragment with
    * @param tag The tag to identify the fragment
     */
    private fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag)
        fragmentTransaction.commit()
    }

    /**
    * This method is called when a new intent is received.
    * It passes the intent to the ClockInOutFragment if it is currently active.
    * @param intent The new intent that was received
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val clockInOutFragment = supportFragmentManager.findFragmentByTag("clock_in_out_fragment") as? ClockInOutFragment
        clockInOutFragment?.handleNfcIntent(intent)
    }
    /**
    * This method is called when an NFC scan is successful.
    * It performs the fragment transaction to replace the current fragment with SuccessfulNfcScanFragment.
     */
    override fun onScanSuccess() {
        // Perform the fragment transaction here
        val newFragment = SuccessfulNfcScanFragment();
        replaceFragment(newFragment,"successful_nfc_scan")
    }
    /**
    This method is used to create a notification channel for Android Oreo and above.
     */
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