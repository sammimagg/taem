package `is`.hi.hbv601g.taem

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class SuccessfulNfcScanFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the MediaPlayer instance and load the sound effect
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_effect)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_succesful_nfc_scan, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Play the sound effect when the fragment becomes visible
        playSoundEffect()
    }


    override fun onDestroy() {
        super.onDestroy()

        // Release the MediaPlayer instance when the fragment is destroyed
        mediaPlayer.release()
    }

    // Call this method to play the sound effect
    private fun playSoundEffect() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

}
