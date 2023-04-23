package `is`.hi.hbv601g.taem

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Networking.getLocalUserFromSharedPreferences
import `is`.hi.hbv601g.taem.Persistance.Transaction
import `is`.hi.hbv601g.taem.Persistance.ViewTransactionUserDAO
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*


/**
* This class represents the Time and Attendance Fragment of the application.
* It displays a screen where the user can view their time and attendance records between two selected dates.
* The class implements various listeners and watchers to update the UI and fetch data from the server.
* @constructor Creates a new instance of TimeAndAttendanceFragment.
 */
class TimeAndAttendanceFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
    * This method is called when the fragment's UI is being created.
    * It inflates the fragment's layout and sets up various listeners and watchers.
    * @param inflater The LayoutInflater used to inflate the fragment's layout.
    * @param container The parent ViewGroup of the fragment's UI.
    * @param savedInstanceState A Bundle containing the saved state of the fragment's UI.
    * @return The View representing the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_time_and_attendace, container, false)
        val dateTextView1 = view.findViewById<TextView>(R.id.newTimeFrom)
        val dateTextView2 = view.findViewById<TextView>(R.id.newTimeTo)
        val user = getLocalUserFromSharedPreferences(requireContext());

        // Set initial dates
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val initialDate1 = "01/${month + 1}/$year"
        dateTextView1.text = initialDate1

        val calendar2 = Calendar.getInstance()
        val year2 = calendar2.get(Calendar.YEAR)
        val month2 = calendar2.get(Calendar.MONTH)
        val day2 = calendar2.get(Calendar.DAY_OF_MONTH)

        val initialDate2 = "${day2}/${month2 + 1}/$year2"
        dateTextView2.text = initialDate2


        var userFromStoreage = getLocalUserFromSharedPreferences(requireContext());
        if(userFromStoreage == null) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
        var ssnToUse = userFromStoreage?.ssn;

        // Add text watchers to date TextViews
        dateTextView1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printSelectedDates(dateTextView1.text.toString(), dateTextView2.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dateTextView2.addTextChangedListener(object : TextWatcher {
            private var debounceJob : Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printSelectedDates(dateTextView1.text.toString(), dateTextView2.text.toString())

                // Cancel the previous debounce job if it exists
                debounceJob?.cancel()

                // Launch a coroutine with a debounce time of 500 milliseconds
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    if(ssnToUse != null) {
                        try {
                            val resultArray: ViewTransactionUserDAO = fetchyTransy(ssnToUse, dateTextView1.text.toString(), dateTextView2.text.toString(), requireContext())
                            val listView = view?.findViewById<ListView>(R.id.realtimeListview1)
                            val apapter = TimeAndAttendanceAdapter(requireActivity(), resultArray.transactionList as ArrayList<Transaction>)
                            listView?.adapter = apapter
                        } catch (e: Exception) {
                            // Handle any errors
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Add click listeners to date TextViews
        dateTextView1.setOnClickListener {
            val day = 1
            val datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateTextView1.text = selectedDate
                }, year, month, day)
            datePickerDialog.show()
        }
        dateTextView2.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateTextView2.text = selectedDate
                }, year2, month2, day2)
            datePickerDialog.show()
        }
        val listView = view.findViewById<ListView>(R.id.realtimeListview1)

        listView.setOnItemClickListener { parent, view, position, id ->
            val transaction = parent.getItemAtPosition(position) as Transaction
            if (transaction != null) {
                Log.d("Debug", "Transaction at position $position: ${transaction.clockInTime}")
                //...
            } else {
                Log.d("Debug", "Transaction is null at position $position")
            }
        }




        return view
    }

    private fun printSelectedDates(startDate: String, endDate: String) {
        println("Start date: $startDate")
        println("End date: $endDate")
    }

    private suspend fun fetchyTransy(ssn : String, startDate: String, endDate: String, context : Context) : ViewTransactionUserDAO {

        return Fetcher().fetchTransactions(
            ssn,
            startDate,
            endDate,
            context
        )
    }
}