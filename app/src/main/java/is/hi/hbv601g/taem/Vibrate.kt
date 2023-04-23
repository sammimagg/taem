package `is`.hi.hbv601g.taem

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Class Vibrate
*/
class Vibrate {

    /**
    * Vibrates the device for a specified amount of time.
    * @param context the context of the calling activity or fragment.
    * @param number the duration of the vibration in milliseconds.
     */
    fun vibrateDevice(context: Context, number: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            val vibrationEffect = VibrationEffect.createOneShot(number, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // For API 25 and below
            vibrator.vibrate(number)
        }
    }
}
