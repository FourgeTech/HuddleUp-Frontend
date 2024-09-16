package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.LoginPageBinding
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
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
                Toast.makeText(this, ToastUtils.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
            }
            else {
                // Sign in the user
                lifecycleScope.launch {
                    val success = UserHelper().signIn(email, password)
                    if (success) {
                        Toast.makeText(this@LoginActivity, ToastUtils.SIGN_IN_SUCCESS, Toast.LENGTH_SHORT).show()
                        openIntent(this@LoginActivity, HomeActivity::class.java)
                    }
                }
            }
        }

        binding.continueWithGoogleButton.setOnClickListener {
            val data = Bundle().apply {
                putString("action", "login")
            }
            openIntent(this, GoogleAuthActivity::class.java,data)
        }

        binding.resetPasswordButton.setOnClickListener{
            openIntent(this, ForgotPasswordActivity::class.java)
        }

    }
}