package `is`.hi.hbv601g.taem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.nio.charset.Charset


class ClockInOutFragment : Fragment() {

    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clock_in_out, container, false)
    }
    private val hceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("ClockInOutFragment", "onReceive called, action: ${intent.action}")
            if (intent.action == "is.hi.hbv601g.taem.HCE_RECEIVED") {
                intent.getByteArrayExtra("commandApdu")?.let { commandApdu ->
                    Log.d("ClockInOutFragment", "Received: ${String(commandApdu)}")
                    // Call the interface method to perform the fragment transaction
                    (activity as? OnScanSuccessListener)?.onScanSuccess()
                }
            }
        }
    }




    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            hceReceiver, IntentFilter("is.hi.hbv601g.taem.HCE_RECEIVED")
        )
        Log.d("ClockInOutFragment", "onResume called")
        Log.d("ClockInOutFragment", "BroadcastReceiver registered")
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(hceReceiver)
        Log.d("ClockInOutFragment", "onPause called")
        Log.d("ClockInOutFragment", "BroadcastReceiver unregistered")
    }
    fun handleNfcIntent(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msg = rawMsgs?.get(0) as NdefMessage
            val record = msg.records[0]
            val payload = record.payload

            // Process the payload as a String
            val data = String(payload, Charset.forName("UTF-8"))
            Log.d("NFC", "Data received: $data")
            print("im here")
        }
    }




}
