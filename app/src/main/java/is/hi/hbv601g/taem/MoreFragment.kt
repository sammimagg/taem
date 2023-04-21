package `is`.hi.hbv601g.taem

import DrivingLogFragment
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
* A fragment that displays additional options and actions available to the user.
 */
class MoreFragment : Fragment() {

    /**
    *Called when the fragment is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    /**
    * Called when the view for the fragment is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    /**
    * Called after the view is created to set up any UI components and event listeners.
     */
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
    /**
    * Helper function to replace the current fragment with a new fragment in the specified container.
     */
    fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            addToBackStack(null)
            commit()
        }
    }
}