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

class ScheduleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // val email_field: TextInputEditText = requireView().findViewById(R.id.profieEmail)
        val employee = getEmployee();
        print(employee)
        if (employee != null) {
            print(employee.firstName)
        };
        val nameOnCard: TextView = requireView().findViewById(R.id.succesful_card_name)
        val ssnOnCard: TextView = requireView().findViewById(R.id.succesful_card_ssn)
        val nameInMessage: TextView = requireView().findViewById(R.id.succesful_card_messages)
        if (employee != null) {
            nameOnCard.text = employee.firstName
            ssnOnCard.text = employee.ssn
            nameInMessage.text = "Thank you have. Have a nice day," + employee.firstName  +"!"

        }

        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }
    private fun getEmployee(): Employee? {
        var employee: Employee? = null
        lifecycleScope.launch {
            employee = fetchEmployeeInfo(requireContext())
        }
        return employee
    }
}
