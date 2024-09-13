package tech.fourge.huddleup_frontend.helpers

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import tech.fourge.huddleup_frontend.CreateAccountActivity
import tech.fourge.huddleup_frontend.HomeActivity


class FirebaseAuthHelper {
    private val auth: FirebaseAuth = Firebase.auth

    // Sign up a new user with email and password
    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                } else {
                    // Registration failed
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, activity: Activity) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    openIntent(activity, HomeActivity::class.java)
                    Log.d(TAG, "Got ID token." + user?.uid)
                } else {
                    // If sign in fails
                    Log.d(TAG, "No ID token!")
                }
            }
    }

    fun signinUser(email: String, password: String, activity: Activity) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    openIntent(activity, HomeActivity::class.java)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }
}