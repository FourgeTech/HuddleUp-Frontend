package tech.fourge.huddleup_frontend.Ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.CreateAccountBinding
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.ValidationUtils


class CreateAccountActivity : AppCompatActivity() {
    // Initialize variables
    lateinit var binding: CreateAccountBinding
    lateinit var email: String
    lateinit var password: String
    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var username: String
    lateinit var role: String
    val validationUtils = ValidationUtils()

    // On Create
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from previous activity
        role = intent.getStringExtra("role").toString()

        // Create Account with Email
        binding.continueButton.setOnClickListener {

            // Check if form submission is valid
            if (!handleFormSubmission()) {
                // If the form submission is not valid, exit the click listener
                return@setOnClickListener
            }

            // If the form submission is valid, proceed to register the user
            lifecycleScope.launch {
                val result = UserHelper().registerUser(email, password, firstname, lastname, role, username)
                if (result == "success") {
                    // If the user is successfully registered, open the HomeActivity
                    UserHelper().signIn(email, password)
                    Toast.makeText(this@CreateAccountActivity, ToastUtils.REGISTRATION_SUCCESS, Toast.LENGTH_SHORT).show()
                    openIntent(this@CreateAccountActivity, JoinCreateTeamActivity::class.java, null, true)
                } else {
                    // If the user is not successfully registered, display an error message
                    Toast.makeText(this@CreateAccountActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Create Account with Google
        binding.continueWithGoogleButton.setOnClickListener {
            val data = Bundle().apply {
                putString("action", "register")
            }
            openIntent(this, GoogleAuthActivity::class.java,data)
        }
    }

    // Handle Form Submission
    private fun handleFormSubmission(): Boolean {
        firstname = binding.inputFirstName.text.toString()
        lastname = binding.inputLastName.text.toString()
        email = binding.inputEmail.text.toString()
        username = binding.inputUsername.text.toString()
        password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputConfirmPassword.text.toString()

        // Check if form fields are empty
        if (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, ToastUtils.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        // Check if email fields is valid
        if(!validationUtils.isValidEmail(email)) {
            Toast.makeText(this, ToastUtils.INVALID_EMAIL_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        // Check if password is valid
        if (!validationUtils.isValidPassword(password)) {
            Toast.makeText(this, ToastUtils.INVALID_PASSWORD_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        // Check if passwords match
        if (!validationUtils.doStringsMatch(binding.inputPassword.text.toString(), binding.inputConfirmPassword.text.toString())) {
            Toast.makeText(this, ToastUtils.PASSWORDS_DONT_MATCH_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        // Check if username is valid
        if (!validationUtils.isValidUsername(username)) {
            Toast.makeText(this, ToastUtils.INVALID_USERNAME_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        // If all checks pass, return true
        return true
    }
}