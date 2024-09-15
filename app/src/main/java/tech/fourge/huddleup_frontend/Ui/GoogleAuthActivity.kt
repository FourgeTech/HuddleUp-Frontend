package tech.fourge.huddleup_frontend.Ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.openIntent

class GoogleAuthActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                lifecycleScope.launch {
                    UserHelper().firebaseAuthWithGoogle(account.idToken!!)
                }.invokeOnCompletion {
                    Log.w(TAG, "Google sign-in successful.")
                    openIntent(this, HomeActivity::class.java)
                }
            } catch (e: ApiException) {
                // Handle different error codes here
                Log.w(TAG, "Google sign-in failed: ${e.statusCode}", e)
                when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                        // User canceled the sign-in
                        Log.w(TAG, "Sign-in was canceled by the user.")
                    }

                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> {
                        // Sign-in failed
                        Log.w(TAG, "Sign-in failed.")
                    }

                    else -> {
                        // General sign-in error
                        Log.w(TAG, "Google sign-in error: ${e.statusCode}", e)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleAuthActivity"
    }
}