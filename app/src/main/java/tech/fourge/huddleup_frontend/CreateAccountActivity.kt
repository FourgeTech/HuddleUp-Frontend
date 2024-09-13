package tech.fourge.huddleup_frontend

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CreateAccountActivity : AppCompatActivity() {
    private var firstNameInput: EditText? = null
    private var lastNameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var confirmPasswordInput: EditText? = null
    private var continueButton: Button? = null
    private var continueWithGoogleButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        // Initialize UI components
        firstNameInput = findViewById<EditText>(R.id.input_first_name)
        lastNameInput = findViewById<EditText>(R.id.input_last_name)
        emailInput = findViewById<EditText>(R.id.input_email)
        passwordInput = findViewById<EditText>(R.id.input_password)
        confirmPasswordInput = findViewById<EditText>(R.id.input_confirm_password)
        continueButton = findViewById<Button>(R.id.continue_button)
        continueWithGoogleButton = findViewById<Button>(R.id.continue_with_google_button)

        // Set onClick listeners
        //continueButton.setOnClickListener(View.OnClickListener { handleFormSubmission() })

        //continueWithGoogleButton.setOnClickListener(View.OnClickListener { handleGoogleSignIn() })
    }

    private fun handleFormSubmission() {
        val firstName = firstNameInput!!.text.toString().trim { it <= ' ' }
        val lastName = lastNameInput!!.text.toString().trim { it <= ' ' }
        val email = emailInput!!.text.toString().trim { it <= ' ' }
        val password = passwordInput!!.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPasswordInput!!.text.toString().trim { it <= ' ' }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Process the form data (e.g., send it to a backend or Firebase)
        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

        // You can also navigate to another activity or save the data
    }

    private fun handleGoogleSignIn() {
        // Handle Google sign-in logic (e.g., using Firebase Authentication)
        Toast.makeText(this, "Google Sign-In clicked", Toast.LENGTH_SHORT).show()
    }
}
