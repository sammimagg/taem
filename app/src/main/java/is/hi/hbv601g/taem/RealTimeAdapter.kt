package `is`.hi.hbv601g.taem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class RealTimeAdapter(private val context: Activity, private val arrayList: Array<Employee>) : ArrayAdapter<Employee>(context,R.layout.real_time_item, arrayList){
    override fun getView(position: Int, convertView: View?,parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.real_time_item,null);

        val fullName : TextView = view.findViewById(R.id.fullName);

        fullName.text = arrayList[position].firstName

        return view;
    }
}