package `is`.hi.hbv601g.taem

import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.Employee
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import `is`.hi.hbv601g.taem.Storage.db
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
            val db2 = db.SessionUserContract.DBHelper(requireContext()).readableDatabase
            val cursor = db2.query(
                `is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                BaseColumns._ID              // The sort order
            )
            with(cursor) {
                moveToLast()
            }
            var ssnToUse = cursor.getString(4)
            var response: Employee = async {
                getEmployeeInformation(
                    "https://www.hiv.is/api/employee/",
                    ssnToUse
                )
            }.await()
            print(response)

            val first_name_field: TextInputEditText =
                requireView().findViewById(R.id.profieFirstname)
            val email_field: TextInputEditText = requireView().findViewById(R.id.profieEmail)
            val username_field: TextInputEditText = requireView().findViewById(R.id.profieUsername)
            val last_name_field: TextInputEditText = requireView().findViewById(R.id.profieLastName)
            val phone_number_field: TextInputEditText =
                requireView().findViewById(R.id.profiePhoneNumber)
            val ssn_field: TextInputEditText = requireView().findViewById(R.id.profieSSN)
            val job_title_field: TextInputEditText = requireView().findViewById(R.id.profieJobtitle)
            val sick_days_field: TextInputEditText = requireView().findViewById(R.id.profieSickDays)
            val vacation_days_field: TextInputEditText =
                requireView().findViewById(R.id.profieVactionDays)
            val start_date_field: TextInputEditText =
                requireView().findViewById(R.id.profileStartDate)
            first_name_field.setText(response.firstName)
            last_name_field.setText(response.lastName)
            email_field.setText(response.email)
            username_field.setText(response.username)
            phone_number_field.setText(response.phoneNumber)
            ssn_field.setText(response.ssn)
            job_title_field.setText(response.jobTitle)
            sick_days_field.setText(response.sickDaysUsed.toString())
            vacation_days_field.setText(response.vacationDaysUsed.toString())
            start_date_field.setText(response.startDate)

            val saveButton = requireView().findViewById<Button>(R.id.buttonProfileSave)
            saveButton.setOnClickListener { saveButtonHandler(response) }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
        //val listView = view.findViewById<ListView>(R.id.employeesListview); //R.id.realtimeListview)
    }

    fun saveButtonHandler(response : Employee) {
        val first_name_field : TextInputEditText = requireView().findViewById(R.id.profieFirstname)
        val email_field : TextInputEditText = requireView().findViewById(R.id.profieEmail)
        val username_field : TextInputEditText = requireView().findViewById(R.id.profieUsername)
        val last_name_field : TextInputEditText = requireView().findViewById(R.id.profieLastName)
        val phone_number_field : TextInputEditText = requireView().findViewById(R.id.profiePhoneNumber)
        val job_title_field : TextInputEditText = requireView().findViewById(R.id.profieJobtitle)
        response.firstName = first_name_field.text.toString()
        response.email = email_field.text.toString()
        response.username = username_field.text.toString()
        response.lastName = last_name_field.text.toString()
        response.phoneNumber = phone_number_field.text.toString()
        response.jobTitle = job_title_field.text.toString()
        val d = Fetcher().postEmployeeProfile("https://www.hiv.is/api/employee/", response, requireContext())

    }

    private suspend fun getEmployeeInformation(url: String, ssn: String) : Employee {
        val context = requireContext()
        val fetcher = Fetcher();
        val response = fetcher.fetchEmployeeProfile(url,ssn,context);
        return response
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}