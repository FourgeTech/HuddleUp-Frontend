package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Models.Settings


class UserHelper {
    // Firebase Auth and Functions instances
    private val auth: FirebaseAuth = Firebase.auth
    private val functions = Firebase.functions

    // On Class Creation
    init {
        // Use the emulator for local development (comment out for production)
        functions.useEmulator("10.0.2.2", 5001)
        auth.useEmulator("10.0.2.2", 9099)
        auth.signOut()
    }

    // Sign up a new user with email and password
    suspend fun registerUser(email: String, password: String, firstname:String, lastname:String, role:String): String {
        return try {
            val data = hashMapOf(
                "email" to email,
                "password" to password,
                "role" to role,
                "firstname" to firstname,
                "lastname" to lastname,
                "username" to "yugen2004",
            )
            // Call the Firebase function for user creation
            val result = functions.getHttpsCallable("createUser").call(data).await()

            // Get the status and message from the result
            val resultData = result.data as? Map<*, *>
            val status = resultData?.get("status") as? String
            val message = resultData?.get("message") as? String

            if (status == "success") {
                "success"
            } else {
                // Return the error message or code
                message ?: "unknown_error"
            }
        }
        catch (e: Exception) {
            Log.w(TAG, "Registration failed", e)
            "unknown_error"
        }
    }

    // Sign in with Google
    suspend fun firebaseAuthWithGoogle(idToken: String): Boolean {
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
    suspend fun signIn(email: String, password: String): String {
        return try {
            // Attempt to sign in with email and password
            auth.signInWithEmailAndPassword(email, password).await()
            "success"
        } catch (e: FirebaseAuthException) {
            // Handle FirebaseAuth specific errors
            Log.w(TAG, "signInWithEmail:failure", e)

            // Extract and return the error code
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Invalid email address."
                "ERROR_WRONG_PASSWORD" -> "Incorrect password."
                "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                "ERROR_USER_DISABLED" -> "User account has been disabled."
                "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Please try again later."
                else -> "Authentication error: ${e.message}"
            }
        } catch (e: Exception) {
            // Handle general exceptions
            Log.w(TAG, "signInWithEmail:failure", e)
            "An unknown error occurred."
        }
    }

    // Check if user exists with Google account
    suspend fun checkIfUserExistsWithGoogleAccount(account: GoogleSignInAccount): Boolean {
        // First, get the email from the Google Sign-In token
        val email = account.email ?: return false

        // Now, check if the email already exists in Firebase Auth
        val signInMethods = auth.fetchSignInMethodsForEmail(email).await()
        return signInMethods.signInMethods?.isNotEmpty() ?: false
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }

    // Reset password
    suspend fun resetPassword(email: String): String {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "Email sent.")
            "success"
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.w(TAG, "error_invalid_user", e)
            "User does not have an account."
        } catch (e: FirebaseAuthException) {
            Log.w(TAG, e.errorCode ?: "unknown_auth_error", e)
            "Password reset failed - Firebase Auth error"
        } catch (e: Exception) {
            Log.w(TAG,"unknown_error" , e)
            "Password reset failed"
        }
    }

    // Create a data class for user settings
    data class UserSettings(
        val name: String? = null,
        val surname: String? = null,
        val profilePicture: String? = null,
        val settings : Settings? = null
    )

    // Function to update user settings
    fun updateUserSettings(settings: UserSettings, callback: (Result<String>) -> Unit) {
        val currentUser = getCurrentUser()
        if (currentUser == null) {
            callback(Result.failure(Exception("No authenticated user")))
            return
        }

        val functions = FirebaseFunctions.getInstance()

        // Map all fields from UserSettings
        val data = mapOf(
            "name" to settings.name,
            "surname" to settings.surname,
            "profilePicture" to settings.profilePicture,
            "settings" to settings.settings // Assuming settings is a custom object
        )

        functions
            .getHttpsCallable("updateUserSettings")
            .call(data)
            .addOnSuccessListener { result ->
                val success = result.data as? Boolean
                if (success == true) {
                    callback(Result.success("Settings updated successfully"))
                } else {
                    callback(Result.failure(Exception("Error updating settings")))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
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