package `is`.hi.hbv601g.taem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RealTimeIngsightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RealTimeIngsightsFragment : Fragment() {

    private lateinit var userArrayList : Array<EmployeeRTI>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_real_time_ingsights,container,false)

        val listView = view.findViewById<ListView>(R.id.realtimeListview);
        lifecycleScope.launch {
            var response = ArrayList<EmployeeRTI>();
            response = async { getRealTimeInsigtArray("https://www.hiv.is/api/employee/rti") }.await()
            //println("Hérna")
            //println(response)
            var sortedList = sortListByActive(response);
            val apapter = RealTimeAdapter(requireActivity(),sortedList);
            listView.adapter =apapter;
        }
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                var response = ArrayList<EmployeeRTI>();
                response = async { getRealTimeInsigtArray("https://www.hiv.is/api/employee/rti") }.await()
                var sortedList = sortListByActive(response);
                val apapter = RealTimeAdapter(requireActivity(),sortedList);
                listView.adapter =apapter;
            }
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }
    private suspend fun getRealTimeInsigtArray(url: String): ArrayList<EmployeeRTI> {
        val context = requireContext()
        val fetcher = Fetcher();
        val response = fetcher.getRealTimeInsights(url,context);
        return response

    }
    private fun sortListByActive (list : ArrayList<EmployeeRTI>): ArrayList<EmployeeRTI> {

        val sortedList = list.sortedWith(Comparator { employee1, employee2 ->
            if (employee1.clockIn && !employee2.clockIn) {
                // employee1 is active and employee2 is inactive
                -1 // return a negative value to indicate that employee1 should come first
            } else if (!employee1.clockIn && employee2.clockIn) {
                // employee1 is inactive and employee2 is active
                1 // return a positive value to indicate that employee1 should come after employee2
            } else {
                // Both employees are active or inactive, or both have a clockOut time set
                employee1.firstName.compareTo(employee2.firstName) // compare the employees based on their first name
            }
        }).toCollection(ArrayList())
        return sortedList
    }

}