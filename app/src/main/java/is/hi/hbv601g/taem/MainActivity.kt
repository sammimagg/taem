package `is`.hi.hbv601g.taem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.taem.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.clockinout -> replaceFragment(ClockInOutFragment())
                R.id.employees -> replaceFragment(ScheduleFragment())
                R.id.time_and_attendance -> replaceFragment(TimeAndAttendanceFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.drivinglog -> replaceFragment(DrivingLogFragment())
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