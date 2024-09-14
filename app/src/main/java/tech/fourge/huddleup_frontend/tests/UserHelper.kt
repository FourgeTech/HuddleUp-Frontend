package tech.fourge.huddleup_frontend.tests

import android.app.Activity
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Ui.HomeActivity
import tech.fourge.huddleup_frontend.Utils.openIntent


class UserHelper {
    private val auth: FirebaseAuth = Firebase.auth

    // Sign up a new user with email and password
    suspend fun registerUser(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.w(TAG, "Registration failed", e)
            false
        }
    }

    // Sign in with Google
    suspend fun firebaseAuthWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            Log.w(TAG, "Google sign-in failed", e)
            false
        }
    }

    // Sign in with email and password
    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "signInWithEmail:success")
            true
        } catch (e: Exception) {
            Log.w(TAG, "signInWithEmail:failure", e)
            false
        }
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }

    // Reset password
    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "Email sent.")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Password reset failed", e)
            false
        }
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    companion object {
        private const val TAG = "UserHelper"
    }
}