// File: app/src/main/java/tech/fourge/huddleup_frontend/Utils/FirebaseUtil.kt
package tech.fourge.huddleup_frontend.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions

object FirebaseUtil {
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val functions: FirebaseFunctions by lazy {
        FirebaseFunctions.getInstance()
    }
}