package tech.fourge.huddleup_frontend.Utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun openIntent(activity: Activity, activityToOpen: Class<*>, data: Bundle? = null, clearTask: Boolean = false) {
    // Declare intent with context and class to pass the value to
    val intent = Intent(activity, activityToOpen)
    // Pass through the string value with key "order"
    data?.let {
        intent.putExtras(it)
    }
    // Set flags if needed to clear task or activity stack
    if (clearTask) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    // Start the activity
    activity.startActivity(intent)
}
/*==========================END OF FILE====================================================================================================================================================*/