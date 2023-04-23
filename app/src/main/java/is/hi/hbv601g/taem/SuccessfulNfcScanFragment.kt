package `is`.hi.hbv601g.taem

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import `is`.hi.hbv601g.taem.Networking.getSessionUser

/**
* A fragment to display a success message and sound effect after a successful NFC scan.
 */
class SuccessfulNfcScanFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer

    /**
    * Called when the fragment is being created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the MediaPlayer instance and load the sound effect
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_effect)
    }

    /**
    * Called when the view for the fragment is being created.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_succesful_nfc_scan, container, false)
    }

    /**
    * Called when the view is created and ready to be displayed.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ssnOnCard: TextView = requireView().findViewById(R.id.succesful_card_ssn);
        val nameOnCard: TextView = requireView().findViewById(R.id.succesful_card_name)
        val message: TextView = requireView().findViewById(R.id.succesful_card_messages);
        val sessionUser = getSessionUser(requireContext())
        if(sessionUser != null) {
            ssnOnCard.text = sessionUser.ssn
            nameOnCard.text = sessionUser.username
            message.text = "Thank you have. Have a nice day, " + sessionUser.username + "!"
        }
        // Play the sound effect when the fragment becomes visible
        playSoundEffect()
    }

    /**
    Called when the fragment is being destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()

        // Release the MediaPlayer instance when the fragment is destroyed
        mediaPlayer.release()
    }

    /**
    * Plays the sound effect.
     */
    private fun playSoundEffect() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

}
