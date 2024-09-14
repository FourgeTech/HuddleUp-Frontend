package tech.fourge.huddleup_frontend.Utils

import android.app.Activity
import android.content.Intent

fun openIntent(activity: Activity, activityToOpen: Class<*>) {
    // declare intent with context and class to pass the value to
    val intent = Intent(activity, activityToOpen)

    // pass through the string value with key "order"
    //intent.putExtra("username", username)

    // start the activity
    activity.startActivity(intent)
}
/*==========================END OF FILE====================================================================================================================================================*/