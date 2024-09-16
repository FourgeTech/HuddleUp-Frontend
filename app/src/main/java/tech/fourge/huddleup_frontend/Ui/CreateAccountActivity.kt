package tech.fourge.huddleup_frontend.Ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.CreateAccountBinding
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Models.UserModel
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.ValidationUtils


class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: CreateAccountBinding
    lateinit var email: String
    lateinit var password: String
    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var role: String
    val validationUtils = ValidationUtils()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                UserHelper().registerUser(email, password, firstname, lastname, role)
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

    private fun handleFormSubmission(): Boolean {
        firstname = binding.inputFirstName.text.toString()
        lastname = binding.inputLastName.text.toString()
        email = binding.inputEmail.text.toString()
        password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputConfirmPassword.text.toString()

        if (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, ToastUtils.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        if(!validationUtils.isValidEmail(email)) {
            Toast.makeText(this, ToastUtils.INVALID_EMAIL_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validationUtils.isValidPassword(password)) {
            Toast.makeText(this, ToastUtils.INVALID_PASSWORD_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }

        if (!validationUtils.doStringsMatch(binding.inputPassword.text.toString(), binding.inputConfirmPassword.text.toString())) {
            Toast.makeText(this, ToastUtils.PASSWORDS_DONT_MATCH_ERROR, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
