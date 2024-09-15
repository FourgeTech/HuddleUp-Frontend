package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.LoginPageBinding
import tech.fourge.huddleup_frontend.tests.UserHelper
import tech.fourge.huddleup_frontend.Utils.openIntent

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(){
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in the user
                lifecycleScope.launch { UserHelper().signIn(email, password) }
            }
        }

        binding.resetPasswordButton.setOnClickListener{
            openIntent(this, ForgotPasswordActivity::class.java)
        }

    }
}