package `is`.hi.hbv601g.taem

import DrivingLogFragment
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MoreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeAndAttendance: LinearLayout = view.findViewById(R.id.time_and_attednace)
        val requestReviews: LinearLayout = view.findViewById(R.id.request_reviews)
        val drivingLog: LinearLayout = view.findViewById(R.id.driving_log)


        timeAndAttendance.setOnClickListener {
            (activity as? AppCompatActivity)?.replaceFragment(TimeAndAttendanceFragment(), R.id.frame_layout);
        }

        requestReviews.setOnClickListener {
            (activity as? AppCompatActivity)?.replaceFragment(RequestReviewFragment(), R.id.frame_layout);
        }

        drivingLog.setOnClickListener {
            (activity as? AppCompatActivity)?.replaceFragment(DrivingLogFragment(), R.id.frame_layout);
        }


    }
    fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            addToBackStack(null)
            commit()
        }
    }


}