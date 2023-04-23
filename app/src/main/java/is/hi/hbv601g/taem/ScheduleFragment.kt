package `is`.hi.hbv601g.taem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import fetchEmployeeInfo
import `is`.hi.hbv601g.taem.Persistance.Employee
import kotlinx.coroutines.launch

/**
* A fragment representing the schedule screen of the application.
* This fragment displays the employee's schedule, obtained from the server.
 */
class ScheduleFragment : Fragment() {

    /**
    * Called to do initial creation of the fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
    * Called to have the fragment instantiate its user interface view.
    * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
    * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
    * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    * @return Returns the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // val email_field: TextInputEditText = requireView().findViewById(R.id.profieEmail)



        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }
    /**
    * Retrieves the employee's information from the server.
    * @return Returns the employee's information.
     */
    private fun getEmployee(): Employee? {
        var employee: Employee? = null
        lifecycleScope.launch {
            employee = fetchEmployeeInfo(requireContext())
        }
        return employee
    }
}
