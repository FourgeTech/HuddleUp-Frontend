package tech.fourge.huddleup_frontend.Utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun openIntent(activity: Activity, activityToOpen: Class<*>, data: Bundle? = null) {
    // declare intent with context and class to pass the value to
    val intent = Intent(activity, activityToOpen)

    // pass through the string value with key "order"
    //intent.putExtra("username", username)
    data?.let {
        intent.putExtras(it)
    }
    // start the activity
    activity.startActivity(intent)
}
/*==========================END OF FILE====================================================================================================================================================*/