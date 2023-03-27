package `is`.hi.hbv601g.taem

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import java.util.*

class EmployessAdaptor(private val context: Activity, private val arrayList: ArrayList<EmployeeRTI>) : ArrayAdapter<EmployeeRTI>(context,R.layout.employess_item, arrayList) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.employess_item,null);
        val fullName : TextView = view.findViewById(R.id.fullName);
        val name = arrayList[position].firstName + " " +  arrayList[position].lastName;
        var typeOfUser : TextView = view.findViewById(R.id.jobTitle);
        typeOfUser.text = "User"
        if(arrayList[position].accountType == 0) {
            typeOfUser.text = "Admin";
        }
        else {
            typeOfUser.text = "User";
        }
        // To trim to long names
        if (name.length <= 17) {
            fullName.text = arrayList[position].firstName + " " +  arrayList[position].lastName;
        }
        else {
            val missMatch = name.length - 17;
            var nameTrimmed = name.substring(0, name.length - missMatch);
            nameTrimmed = "$nameTrimmed.";
            fullName.text = nameTrimmed
        }

        return view;
    }
}