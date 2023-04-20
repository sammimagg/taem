package `is`.hi.hbv601g.taem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.MappedRequestUserDAO
import `is`.hi.hbv601g.taem.Persistance.Transaction
import kotlinx.coroutines.*

class RequestReviewFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var debounceJob : Job? = null;
        debounceJob?.cancel()
        debounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(500) // debounce for 500 milliseconds
            // Inflate the layout for this fragment
            val reval = Fetcher().fetchPendingReviews("https://www.hiv.is/api/review", requireContext())

            for (item in reval) {
                print(item.toString())
            }
            val listView = view.findViewById<ListView>(R.id.realtimeListview2);
            val apapter = TimeAndAttendanceAdapter(requireActivity(),
                reval as ArrayList<MappedRequestUserDAO>
            );
            listView.adapter =apapter;
        }
        return inflater.inflate(R.layout.fragment_request_reviews, container, false)
    }
}
