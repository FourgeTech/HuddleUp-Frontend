package tech.fourge.huddleup_frontend.Ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.openIntent

class GoogleAuthActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInAccount: GoogleSignInAccount
    lateinit var action: String

    // On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action = intent.getStringExtra("action").toString()
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

    // On Activity Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            // Get google account from sign in
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                googleSignInAccount = task.getResult(ApiException::class.java)!!
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed: ${e.statusCode}", e)
            }

            // If action is login then check if user exists with Google account
            if(action == "login"){
                lifecycleScope.launch {
                    if (UserHelper().checkIfUserExistsWithGoogleAccount(googleSignInAccount)) {
                        // If exists Login
                        createAccountWithGoogle(googleSignInAccount)
                    }
                    else{
                        Toast.makeText(this@GoogleAuthActivity, ToastUtils.ACCOUNT_DOES_NOT_EXIST_ERROR, Toast.LENGTH_SHORT).show()
                        openIntent(this@GoogleAuthActivity, LoginActivity::class.java)
                    }
                }
            }
            // If action is register then create account with Google
            else if(action == "register"){
                // Create new account with Google
                lifecycleScope.launch {
                    createAccountWithGoogle(googleSignInAccount)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Create account with Google
    fun createAccountWithGoogle(account: GoogleSignInAccount)
    {
        lifecycleScope.launch {
            val result = UserHelper().firebaseAuthWithGoogle(account.idToken!!)
            if (result) {
                openIntent(this@GoogleAuthActivity, HomeActivity::class.java)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleAuthActivity"
    }
}