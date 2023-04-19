package `is`.hi.hbv601g.taem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import fetchEmployeeInfo
import kotlinx.coroutines.launch


class MakeScheduleFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(        inflater: LayoutInflater, container: ViewGroup?,        savedInstanceState: Bundle?    ): View? {
        // Inflate the layout for this fragment
        lifecycleScope.launch {
            print(fetchEmployeeInfo(requireContext()))
        }
        return inflater.inflate(R.layout.fragment_driving_log, container, false)
    }
}
