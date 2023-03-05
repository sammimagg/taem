package `is`.hi.hbv601g.taem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.taem.databinding.ActivityMainAdminBinding

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.clockinout -> replaceFragment(ClockInOutFragment())
                R.id.employees -> replaceFragment(EmployeesFragment())
                R.id.real_time_insights -> replaceFragment(RealTimeIngsightsFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.more -> replaceFragment(MoreFragment())
                else -> {

                }
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}