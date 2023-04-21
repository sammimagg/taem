package `is`.hi.hbv601g.taem

import `is`.hi.hbv601g.taem.Networking.Fetcher
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.hbv601g.taem.Persistance.Employee
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class EmployeesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_employees,container,false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        val listView = view.findViewById<ListView>(R.id.employeesListview)

        // Set up the list view adapter and item click listener
        lifecycleScope.launch {
            var response = ArrayList<Employee>()
            response = async { getEmployeeArray() }.await()

            val adapter = EmployessAdaptor(requireActivity(), response)
            listView.adapter = adapter
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected employee
            val employee = parent.getItemAtPosition(position) as Employee

            // Create a new instance of the EditEmployeeFragment and pass the employee as an argument
            val editEmployeeFragment = EditEmployeeFragment.newInstance(employee)

            // Navigate to the EditEmployeeFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, editEmployeeFragment)
                .addToBackStack(null)
                .commit()
        }

        // Set up the swipe refresh layout listener
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                var response = ArrayList<Employee>()
                response = async { getEmployeeArray() }.await()

                val adapter = EmployessAdaptor(requireActivity(), response)
                listView.adapter = adapter
            }
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    private suspend fun getEmployeeArray(): ArrayList<Employee> {
        val context = requireContext()
        val fetcher = Fetcher()
        val response = fetcher.getEmployeeList(context)
        return response
    }
}
