package `is`.hi.hbv601g.taem

import android.content.Context
import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.nio.charset.Charset
import android.os.Vibrator
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import `is`.hi.hbv601g.taem.Vibrate;

class MyHostApduService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d("MyHostApduService:HCE", "processCommandApdu")
        Log.d("MyHostApduService:HCE2", commandApdu.contentToString())


        val receivedString = commandApdu.toString(Charset.forName("UTF-8"))
        Log.d("MyHostApduService:HCE", "Received string: $receivedString")

        // Check if the received string contains the expected phrases
        if (receivedString.contains("G'bye!") || receivedString.contains("G'day!")) {
            // Send the local broadcast with the received data
            val intent = Intent("is.hi.hbv601g.taem.HCE_RECEIVED")
            intent.putExtra("commandApdu", commandApdu)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

            // Vibrate the device for 300 milliseconds
            val vibrate = Vibrate()
            vibrate.vibrateDevice(this, 300)
        }

        return "ssn=1602942809".toByteArray()
    }

    override fun onDeactivated(reason: Int) {
        Log.d("MyHostApduService:HCE", "Deactivated: $reason")
    }
}