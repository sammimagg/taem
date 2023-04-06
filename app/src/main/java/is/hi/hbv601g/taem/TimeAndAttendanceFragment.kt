package `is`.hi.hbv601g.taem

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.ViewTransactionUserDAO
import java.time.LocalDate


class TimeAndAttendanceFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_time_and_attendace, container, false)
        val dateTextView1 = view.findViewById<TextView>(R.id.date_text_view1)
        val dateTextView2 = view.findViewById<TextView>(R.id.date_text_view2)

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

        // Add text watchers to date TextViews
        dateTextView1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printSelectedDates(dateTextView1.text.toString(), dateTextView2.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dateTextView2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printSelectedDates(dateTextView1.text.toString(), dateTextView2.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
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

        return view
    }

    private fun printSelectedDates(startDate: String, endDate: String) {
        println("Start date: $startDate")
        println("End date: $endDate")
    }

    private suspend fun fetchyTransy(ssn : String, startDate: String,
                                     endDate: String, context : Context) : ViewTransactionUserDAO {
        return Fetcher().fetchTransactions(
            "https://www.hiv.is/api/transaction/list",
            ssn,
            startDate,
            endDate,
            context
        )
    }






}