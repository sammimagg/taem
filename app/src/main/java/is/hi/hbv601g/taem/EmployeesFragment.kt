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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
* A fragment that displays a list of employees and allows the user to edit their information.
 */
class EmployeesFragment : Fragment() {


    /**
    * Called to do initial creation of a fragment. This is called after onAttach and before onCreateView.
    * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
    * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null.
    * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
    * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
    * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    * @return Return the View for the fragment's UI, or null.
     */
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

    /**
    * Retrieves a list of employees from the server.
    * @return An ArrayList of Employee objects.
     */
    private suspend fun getEmployeeArray(): ArrayList<Employee> {
        val context = requireContext()
        val fetcher = Fetcher()
        val response = fetcher.getEmployeeList(context)
        return response
    }
}
