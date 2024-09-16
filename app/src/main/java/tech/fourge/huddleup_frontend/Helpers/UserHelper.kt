package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await


class UserHelper {
    private val auth: FirebaseAuth = Firebase.auth
    private val functions = Firebase.functions

    // Sign up a new user with email and password
    suspend fun registerUser(email: String, password: String): Boolean {
        functions.useEmulator("10.0.2.2", 5001)
        return try {
            val data = hashMapOf(
                "email" to email,
                "password" to password,
                "role" to "manager",
                "name" to "Yugen",
                "surname" to "Naidoo",
                "username" to "yugen2004",
            )
            functions.getHttpsCallable("createUser").call(data).await()
            true
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Registration failed firebase: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.w(TAG, "Registration failed", e)
            false
        }
    }

    // Sign in with Google
    suspend fun firebaseAuthWithGoogle(idToken: String): Boolean {
        functions.useEmulator("10.0.2.2", 5001)
        auth.useEmulator("10.0.2.2", 9099)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        val user = auth.currentUser
        return try {
            val data = hashMapOf(
                "uid" to user?.uid,
                "email" to user?.email,
                "username" to user?.displayName,
                "role" to "manager",
            )
            functions.getHttpsCallable("createUserWithGoogle").call(data).await()
            true
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Registration failed firebase: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.w(TAG, "Registration failed", e)
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