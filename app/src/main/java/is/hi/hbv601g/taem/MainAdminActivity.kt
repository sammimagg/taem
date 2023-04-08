package `is`.hi.hbv601g.taem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.taem.databinding.ActivityMainAdminBinding

class MainAdminActivity : AppCompatActivity(), OnScanSuccessListener {
    private lateinit var binding: ActivityMainAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
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



}