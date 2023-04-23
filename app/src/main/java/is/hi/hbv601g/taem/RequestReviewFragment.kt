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
import `is`.hi.hbv601g.taem.Persistance.MappedRequestUserDAO
import `is`.hi.hbv601g.taem.Persistance.Transaction
import kotlinx.coroutines.*

/**
* A Fragment to display a list of pending review requests fetched from the server.
 */
class RequestReviewFragment : Fragment() {

    /**
    * Called to do initial creation of the fragment.
    * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    /**
    * Called to have the fragment instantiate its user interface view.
    * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
    * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
    * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_request_reviews, container, false)
        var debounceJob : Job? = null;
        debounceJob?.cancel()
        debounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(500) // debounce for 500 milliseconds
            // Inflate the layout for this fragment
            val reval = Fetcher().fetchPendingReviews( requireContext())

            for (item in reval) {
                print(item.toString())
            }
            val listView = view.findViewById<ListView>(R.id.realtimeListview2);
            val apapter = RequestReviewAdapter(requireActivity(),
                reval as ArrayList<MappedRequestUserDAO>
            );
            listView.adapter =apapter;

        }
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch{
                val reval = Fetcher().fetchPendingReviews( requireContext())

                for (item in reval) {
                    print(item.toString())
                }
                val listView = view.findViewById<ListView>(R.id.realtimeListview2);
                val apapter = RequestReviewAdapter(requireActivity(),
                    reval as ArrayList<MappedRequestUserDAO>
                );
                listView.adapter =apapter;
            }
            swipeRefreshLayout.isRefreshing = false

        }

        return view
    }
}
