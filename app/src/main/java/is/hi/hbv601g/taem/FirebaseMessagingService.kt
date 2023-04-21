package `is`.hi.hbv601g.taem
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Log the incoming message payload
        Log.d("MyFirebaseMessagingService", "From: ${remoteMessage.from}")
        remoteMessage.data.forEach { (key, value) ->
            Log.d("MyFirebaseMessagingService", "$key: $value")
        }

        // Create an Intent to launch an Activity in response to the message
        val intent = Intent(this, TimeAndAttendanceFragment::class.java).apply {
            // Add any necessary extras to the Intent
            putExtra("key", "value")
        }

        // Create a PendingIntent for the Activity
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Create a notification using the received data and the PendingIntent
        val notificationBuilder = NotificationCompat.Builder(this, "nfcscann")
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["body"])
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
