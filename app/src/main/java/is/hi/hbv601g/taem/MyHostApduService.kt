package `is`.hi.hbv601g.taem


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import java.nio.charset.Charset
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import `is`.hi.hbv601g.taem.Networking.PREF_NAME
import `is`.hi.hbv601g.taem.Networking.getLocalUserFromSharedPreferences
import `is`.hi.hbv601g.taem.Persistance.Employee


/**
* A HostApduService for handling communication between an Android device and an external NFC reader.
* This service is responsible for processing incoming commands sent by the NFC reader and sending responses back to it.
* It also shows a notification and vibrates the device when specific commands are received.
* @constructor Creates a new instance of MyHostApduService.
 */
class MyHostApduService : HostApduService() {


    /**
    * This method is called when a command is received from the NFC reader.
    * @param commandApdu the command received from the NFC reader
    * @param extras additional data associated with the command
    * @return a byte array containing the response to the command
     */
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

        val prefs = applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val employeeJson = prefs.getString("employee", null)
        val employee = Gson().fromJson(employeeJson, Employee::class.java)
        val ssn = employee?.ssn ?: null

        Log.d("MyHostApduService:HCE", "processCommandApdu")
        Log.d("MyHostApduService:HCE2", commandApdu.contentToString())
        if (ssn != null) {
            Log.d("SSN from local storage", ssn)
            val receivedString = commandApdu.toString(Charset.forName("UTF-8"))
            Log.d("MyHostApduService:HCE", "Received string: $receivedString")

            // Check if the received string contains the expected phrases
            if (receivedString.contains("G'bye!") || receivedString.contains("G'day!")) {
                // Send the local broadcast with the received data
                val intent = Intent("is.hi.hbv601g.taem.HCE_RECEIVED")
                intent.putExtra("commandApdu", commandApdu)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                showNotification("Tæm", receivedString)
                // Vibrate the device for 300 milliseconds
                val vibrate = Vibrate()
                vibrate.vibrateDevice(this, 300)
            }

            return "ssn=$ssn".toByteArray()
        }
        return "".toByteArray();
    }

    /**
    * This method is called when the service is deactivated, either by the system or the NFC reader.
    * @param reason the reason for deactivation
     */
    override fun onDeactivated(reason: Int) {
        Log.d("MyHostApduService:HCE", "Deactivated: $reason")
    }

    /**
    * Displays a notification with the specified title and message.
    * @param title the title of the notification
    * @param message the message to display in the notification
     */
    private fun showNotification(title: String, message: String) {
        val notificationId = 100

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, "nfcscann")
            .setSmallIcon(R.drawable.logo_1_)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

}