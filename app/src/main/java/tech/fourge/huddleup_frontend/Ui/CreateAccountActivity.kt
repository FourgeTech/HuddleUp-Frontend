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


class CreateAccountActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueButton.setOnClickListener{
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Register the user
                lifecycleScope.launch { UserHelper().registerUser(email, password) }
                    }
                }

        binding.continueWithGoogleButton.setOnClickListener{
            openIntent(this, GoogleAuthActivity::class.java)
        }
    }
}
