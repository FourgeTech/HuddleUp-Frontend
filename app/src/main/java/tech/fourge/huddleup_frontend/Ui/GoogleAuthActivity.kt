package tech.fourge.huddleup_frontend.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.BuildConfig
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.openIntent

class GoogleAuthActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInAccount: GoogleSignInAccount
    lateinit var action: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        action = intent.getStringExtra("action").toString()
        Log.d(TAG, "Action: $action")

        Log.d(TAG, "Google Sign-In initiated: ${BuildConfig.GOOGLE_CLIENT_ID}")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                googleSignInAccount = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Google Sign-In successful: ${googleSignInAccount.email}")

                if (action == "login") {
                    lifecycleScope.launch {
                        if (UserHelper().checkIfUserExistsWithGoogleAccount(googleSignInAccount)) {
                            createAccountWithGoogle(googleSignInAccount)
                        } else {
                            Toast.makeText(this@GoogleAuthActivity, ToastUtils.ACCOUNT_DOES_NOT_EXIST_ERROR, Toast.LENGTH_SHORT).show()
                            openIntent(this@GoogleAuthActivity, LoginActivity::class.java)
                        }
                    }
                } else if (action == "register") {
                    lifecycleScope.launch {
                        createAccountWithGoogle(googleSignInAccount)
                    }
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign-in failed: ${e.statusCode}", e)
                Toast.makeText(this, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createAccountWithGoogle(account: GoogleSignInAccount) {
        lifecycleScope.launch {
            val result = UserHelper().firebaseAuthWithGoogle(account.idToken!!)
            if (result) {
                Toast.makeText(this@GoogleAuthActivity, ToastUtils.SIGN_IN_SUCCESS, Toast.LENGTH_SHORT).show()
                openIntent(this@GoogleAuthActivity, HomeActivity::class.java, null, true)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleAuthActivity"
    }
}