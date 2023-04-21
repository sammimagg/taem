package `is`.hi.hbv601g.taem

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import `is`.hi.hbv601g.taem.Networking.Fetcher
import `is`.hi.hbv601g.taem.Persistance.MappedRequestUserDAO
import kotlinx.coroutines.*

class RequestReviewAdapter(private val context: Activity, private val arrayList: ArrayList<MappedRequestUserDAO>) :
    ArrayAdapter<MappedRequestUserDAO>(context, R.layout.request_item, arrayList), View.OnTouchListener {

    private var downX: Float = 0.toFloat()
    private var swipeLayout: RelativeLayout? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.request_item, null)
        val reqId: TextView = view.findViewById(R.id.request_id)
        val userKennitala: TextView = view.findViewById(R.id.user_kennitala2)
        val userName: TextView = view.findViewById(R.id.user_first_name)
        val oldDate: TextView = view.findViewById(R.id.old_transaction_date)
        val newDate: TextView = view.findViewById(R.id.transaction_date)
        val oldClockIn: TextView = view.findViewById(R.id.old_transaction_clock_in)
        val oldClockOut: TextView = view.findViewById(R.id.old_transaction_clock_out)
        val newClockIn: TextView = view.findViewById(R.id.transaction_clock_in)
        val newClockOut: TextView = view.findViewById(R.id.transaction_clock_out)

        // Set the data for each TextView
        reqId.text = arrayList[position].transactionReview.id.toString()
        userKennitala.text = arrayList[position].employee.ssn
        userName.text = arrayList[position].employee.firstName
        oldDate.text = arrayList[position].transactionReview.clockInDate
        newDate.text = arrayList[position].transactionReview.clockInDate
        oldClockIn.text = arrayList[position].transactionReview.originalClockInTime
        oldClockOut.text = arrayList[position].transactionReview.originalClockOutTime
        newClockIn.text = arrayList[position].transactionReview.changedClockInTime
        newClockOut.text = arrayList[position].transactionReview.changedClockOutTime

        // Get the swipe layout and set the onTouchListener on it
        swipeLayout = view.findViewById(R.id.relative_layout_id)
        swipeLayout!!.setOnTouchListener(this)

        return view
    }
    private var isAnimatingOffScreen = false

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = motionEvent.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val translationX = motionEvent.rawX - downX
                view.translationX = translationX
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (!isAnimatingOffScreen) {
                    val translationX = view.translationX
                    val viewWidth = view.width
                    val threshold = viewWidth/64
                    if (translationX > threshold) {
                        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translationX, viewWidth.toFloat())
                        animator.duration = 100
                        animator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {
                                view.visibility = View.GONE
                                Log.d("translationX", translationX.toString())
                                val reqIdView = view.findViewById<TextView>(R.id.request_id)
                                val reqId = reqIdView.text.toString()
                                onSwipeComplete(view, "RIGHT",reqId,context)
                            }
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                        animator.start()
                    }
                    else if(translationX < 0) {
                        val tes: Double = 10000.0
                        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, viewWidth.toFloat(), tes.toFloat())
                        animator.duration = 100
                        animator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {

                                view.visibility = View.GONE
                                val reqIdView = view.findViewById<TextView>(R.id.request_id)
                                val reqId = reqIdView.text.toString()
                                onSwipeComplete(view, "LEFT",reqId,context)
                            }
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                        animator.start()
                    }
                    else {
                        // Slide back to original position
                        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translationX, 0f)
                        animator.duration = 200
                        animator.start()
                    }
                }
                return true
            }
        }
        return false
    }
}
private fun onSwipeComplete(view: View, direction: String,reqId:String,context: Context) {
    val userName: TextView = view.findViewById(R.id.user_first_name)

    when(direction) {
        "LEFT" -> {
            Fetcher().handleReviewRequest(
                (reqId).toLong(), context, "false")
            showAlert("Rejected request", context)
            println("swiped left")
        }
        "RIGHT" -> {
            Fetcher().handleReviewRequest(
                (reqId).toLong(), context, "true")
            showAlert("Approved request", context)

        }
    }
    view.translationX = 0f // Set translationX back to 0
}

private fun showAlert(message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Swipe Event")
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialog, which ->
        // Do something when the positive button is clicked
    }
    builder.show()
}