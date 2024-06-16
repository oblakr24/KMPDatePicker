package data

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

actual class IntentHandler(private var activityRef: WeakReference<ComponentActivity>?) {
    actual fun openURL(url: String) {
        activityRef?.get()?.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            })
    }

}