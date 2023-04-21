package `is`.hi.hbv601g.taem

import android.content.Context
import android.content.Intent
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
import `is`.hi.hbv601g.taem.Networking.*
import `is`.hi.hbv601g.taem.Persistance.Employee
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import `is`.hi.hbv601g.taem.Storage.db
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
* A Fragment subclass that represents the user profile screen.
 */
class ProfileFragment : Fragment() {

    /**
    * Called to do initial creation of a fragment. This is called after the Activity's onCreate method
    * has returned.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
    * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical
    * fragments can return null (which is the default implementation). This will be called between onCreate(Bundle)
    * and onActivityCreated(Bundle).
    * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
    * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
    * The fragment should not add the view itself, but this can be used to generate markdown the LayoutParams of the view.
    * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val context = requireContext();
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val logOutButton = rootView.findViewById<Button>(R.id.logout);
        logOutButton.setOnClickListener{logOut(context)}
        val user = getLocalUserFromSharedPreferences(requireContext())

        val first_name_field: TextInputEditText = rootView.findViewById(R.id.profieFirstname)
        val email_field: TextInputEditText = rootView.findViewById(R.id.profieEmail)
        val username_field: TextInputEditText = rootView.findViewById(R.id.profieUsername)
        val last_name_field: TextInputEditText = rootView.findViewById(R.id.profieLastName)
        val phone_number_field: TextInputEditText = rootView.findViewById(R.id.profiePhoneNumber)
        val ssn_field: TextInputEditText = rootView.findViewById(R.id.profieSSN)
        val job_title_field: TextInputEditText = rootView.findViewById(R.id.profieJobtitle)
        val sick_days_field: TextInputEditText = rootView.findViewById(R.id.profieSickDays)
        val vacation_days_field: TextInputEditText = rootView.findViewById(R.id.profieVactionDays)
        val start_date_field: TextInputEditText = rootView.findViewById(R.id.profileStartDate)
        if(user != null) {
            first_name_field.setText(user.firstName)
            last_name_field.setText(user.lastName)
            email_field.setText(user.email)
            username_field.setText(user.username)
            phone_number_field.setText(user.phoneNumber)
            ssn_field.setText(user.ssn)
            job_title_field.setText(user.jobTitle)
            sick_days_field.setText(user.sickDaysUsed.toString())
            vacation_days_field.setText(user.vacationDaysUsed.toString())
            start_date_field.setText(user.startDate)
            val saveButton = rootView.findViewById<Button>(R.id.buttonProfileSave)
            saveButton.setOnClickListener { saveButtonHandler(user) }
        }
        return rootView
    }
    /**
    * Handler for the save button. Saves the changes made by the user to their profile in the local storage
    * and also sends the updated profile to the server.
    * @param response The Employee object containing the user's profile data.
     */
    fun saveButtonHandler(response : Employee) {
        val first_name_field : TextInputEditText = requireView().findViewById(R.id.profieFirstname)
        updateLocalUserInSharedPreferences(requireContext(), "firstName", first_name_field.text.toString())
        val email_field : TextInputEditText = requireView().findViewById(R.id.profieEmail)
        updateLocalUserInSharedPreferences(requireContext(), "email",email_field.text.toString() )
        val username_field : TextInputEditText = requireView().findViewById(R.id.profieUsername)
        updateLocalUserInSharedPreferences(requireContext(), "username", username_field.text.toString())
        val last_name_field : TextInputEditText = requireView().findViewById(R.id.profieLastName)
        updateLocalUserInSharedPreferences(requireContext(), "lastName", last_name_field.text.toString())
        val phone_number_field : TextInputEditText = requireView().findViewById(R.id.profiePhoneNumber)
        updateLocalUserInSharedPreferences(requireContext(), "phoneNumber", phone_number_field.text.toString())
        val job_title_field : TextInputEditText = requireView().findViewById(R.id.profieJobtitle)
        updateLocalUserInSharedPreferences(requireContext(), "jobTitle", job_title_field.text.toString())
        response.firstName = first_name_field.text.toString()
        response.email = email_field.text.toString()
        response.username = username_field.text.toString()
        response.lastName = last_name_field.text.toString()
        response.phoneNumber = phone_number_field.text.toString()
        response.jobTitle = job_title_field.text.toString()
        saveLocalUserInSharedPreferences(requireContext(), response)
        val d = Fetcher().postEmployeeProfile( response, requireContext())
    }
    fun logOut(context: Context) {
        clearLocalUserFromSharedPreferences(context)
        clearUserData(context);
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()

    }

    private suspend fun getEmployeeInformation( ssn: String) : Employee {
        val context = requireContext()
        val fetcher = Fetcher();
        val response = fetcher.fetchEmployeeProfile(ssn,context);
        return response
    }
}