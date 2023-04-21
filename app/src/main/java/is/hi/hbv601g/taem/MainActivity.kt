package `is`.hi.hbv601g.taem

import DrivingLogFragment
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.taem.databinding.ActivityMainBinding
import java.nio.charset.Charset

/**
* This is the MainActivity class, responsible for managing the main user interface of the application.
* This class extends AppCompatActivity and overrides the onCreate() method to set up the user interface,
* and onNewIntent() method to handle the NFC intent.
* This class contains the following functions:
* onCreate() - sets up the UI and handles user interactions
* replaceFragment() - replaces the current fragment with a new fragment
* onNewIntent() - handles the NFC intent when the application is already running
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ClockInOutFragment(), "clock_in_out_fragment")
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.clockinout -> replaceFragment(ClockInOutFragment(), "clock_in_out_fragment")
                R.id.time_and_attendance -> replaceFragment(TimeAndAttendanceFragment(),"time_and_attendance")
                R.id.profile -> replaceFragment(ProfileFragment(), "profile_fragment")
                R.id.drivinglog -> replaceFragment(DrivingLogFragment(),"driving_log")
                else -> {
                }
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
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msg = rawMsgs?.get(0) as NdefMessage
            val record = msg.records[0]
            val payload = record.payload

            // Process the payload as a String
            val data = String(payload, Charset.forName("UTF-8"))
            Log.d("NFC", "Data received: $data")
        }
    }
}