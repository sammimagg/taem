package `is`.hi.hbv601g.taem

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import `is`.hi.hbv601g.taem.Persistance.Employee
import java.util.*

/**
* A custom ArrayAdapter class that displays a list of employees in a ListView.
* @param context the activity context
* @param arrayList the list of employees to be displayed
 */
class EmployessAdaptor(private val context: Activity, private val arrayList: ArrayList<Employee>) : ArrayAdapter<Employee>(context,R.layout.employess_item, arrayList) {

    /**
    *Creates a View for the specified position in the ListView.
    *@param position the position of the item in the list
    *@param convertView the old view to reuse, if possible
    *@param parent the parent view that the new view will be attached to
    *@return the created View for the specified position
     */

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