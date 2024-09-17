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

        // Sign in the user
        binding.btnLogin.setOnClickListener(){

            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            // Check if email and password are empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, ToastUtils.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
            }
            else {
                // Sign in the user
                lifecycleScope.launch {
                    val result = UserHelper().signIn(email, password)
                    if (result == "success") {
                        Toast.makeText(this@LoginActivity, ToastUtils.SIGN_IN_SUCCESS, Toast.LENGTH_SHORT).show()
                        openIntent(this@LoginActivity, HomeActivity::class.java,null,true)
                    }
                    else{
                        Toast.makeText(this@LoginActivity, result, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Continue with Google single sign-on
        binding.continueWithGoogleButton.setOnClickListener {
            // Pass data to the GoogleAuthActivity
            val data = Bundle().apply {
                putString("action", "login")
            }
            openIntent(this, GoogleAuthActivity::class.java,data)
        }

        // Open the CreateAccountActivity
        binding.resetPasswordButton.setOnClickListener{
            openIntent(this, ForgotPasswordActivity::class.java)
        }

    }
}