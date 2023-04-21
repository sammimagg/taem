package `is`.hi.hbv601g.taem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.Employee
import android.widget.ArrayAdapter
import android.widget.Spinner


/**
* A fragment that allows the user to edit an employee's profile.
 */
class EditEmployeeFragment : Fragment() {

    /**
    * Called to do initial creation of the fragment.
    * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
    *Called to have the fragment instantiate its user interface view.
    *@param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
    *@param container If non-null, this is the parent view that the fragment's UI should be attached to.
    *@param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    *@return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_employee, container, false)
        val firstNameEditText = view.findViewById<TextInputEditText>(R.id.profieFirstname)
        val lastNameEditText = view.findViewById<TextInputEditText>(R.id.profieLastName)
        val usernameEditText = view.findViewById<TextInputEditText>(R.id.profieUsername)
        val emailEditText = view.findViewById<TextInputEditText>(R.id.profieEmail)
        val phoneNumberEditText = view.findViewById<TextInputEditText>(R.id.profiePhoneNumber)
        val ssnEditText = view.findViewById<TextInputEditText>(R.id.profieSSN)
        val jobTitleEditText = view.findViewById<TextInputEditText>(R.id.profieJobtitle)
        val sickDaysEditText = view.findViewById<TextInputEditText>(R.id.profieSickDays)
        val vacationDaysEditText = view.findViewById<TextInputEditText>(R.id.profieVactionDays)
        val startDateEditText = view.findViewById<TextInputEditText>(R.id.profileStartDate)
        val saveButton = view.findViewById<Button>(R.id.buttonProfileSave)
        val cancelButton = view.findViewById<Button>(R.id.buttonProfileCancel)
        val spinner: Spinner = view.findViewById(R.id.employeeRole)

        val employee = arguments?.getParcelable<Employee>("employee")
        val roles = arrayOf("Admin",  "User")

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        if (employee != null) {

            firstNameEditText.setText(employee.firstName)
            lastNameEditText.setText(employee.lastName)
            usernameEditText.setText(employee.username)
            emailEditText.setText(employee.email)
            phoneNumberEditText.setText(employee.phoneNumber)
            ssnEditText.setText(employee.ssn)
            jobTitleEditText.setText(employee.jobTitle)
            startDateEditText.setText(employee.startDate)
            if(employee.accountType == 0) {
                Log.d("EditEmployeeFragment", "Setting spinner selection to Admin")
                spinner.setSelection(0)
            } else if (employee.accountType == 2) {
                Log.d("EditEmployeeFragment", "Setting spinner selection to User")
                spinner.setSelection(1)
            }

        }

        saveButton.setOnClickListener {
            if (employee != null) {
                employee.firstName = firstNameEditText.text.toString()
                employee.lastName = lastNameEditText.text.toString()
                employee.username = usernameEditText.text.toString()
                employee.email = emailEditText.text.toString()
                employee.phoneNumber = phoneNumberEditText.text.toString()
                employee.ssn = ssnEditText.text.toString()
                employee.jobTitle = jobTitleEditText.text.toString()
                employee.startDate = startDateEditText.text.toString()

                val selectedValue = spinner.selectedItem as String
                employee.accountType = if (selectedValue == "Admin") 0 else 2
                Log.d("EditEmployeeFragment", "New account type: " + employee.accountType.toString())
                // Call a function to update the employee information in the database
                Fetcher().postEmployeeProfile( employee, requireContext())
            }
        }

        cancelButton.setOnClickListener {
            // Go back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        fun newInstance(employee: Employee): EditEmployeeFragment {
            val fragment = EditEmployeeFragment()
            val args = Bundle()
            args.putParcelable("employee", employee)
            fragment.arguments = args
            return fragment
        }
    }
}