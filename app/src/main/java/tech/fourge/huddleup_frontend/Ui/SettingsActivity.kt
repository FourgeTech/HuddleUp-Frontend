package tech.fourge.huddleup_frontend.Ui;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.auth.FirebaseAuth
import tech.fourge.huddleup_frontend.databinding.SettingsPageBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsPageBinding
    private val functions = FirebaseFunctions.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener { saveSettings() }
        binding.buttonCancel.setOnClickListener { finish() }
    }

    private fun saveSettings() {
        val name = binding.editName.text.toString()
        val surname = binding.editSurname.text.toString()
        val profilePicture = binding.editProfilePicture.text.toString()

        val userSettings = hashMapOf(
            "firstname" to name,
            "lastname" to surname,
            "profilePicUrl" to profilePicture
            // Add other settings if needed
        )

        val user = auth.currentUser
        if (user != null) {
            functions
                .getHttpsCallable("updateUserSettings")
                .call(userSettings)
                .addOnSuccessListener { result ->
                    // Handle success
                    // Show a success message or navigate back
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    // Show an error message
                }
        } else {
            // Handle the case where the user is not authenticated
        }
    }
}
