package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.ForgotPasswordPageBinding
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.ValidationUtils
import tech.fourge.huddleup_frontend.Utils.openIntent

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ForgotPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reset the user's password
        binding.resetPasswordButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            if (email.isEmpty()) {
                binding.emailEditText.error = "Email is required"
                return@setOnClickListener
            }
            if (!ValidationUtils().isValidEmail(email)) {
                binding.emailEditText.error = "Please Enter a valid email"
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val result = UserHelper().resetPassword(email)
                if (result == "success") {
                    binding.emailEditText.error = null
                    binding.emailEditText.setText("")
                    Toast.makeText(this@ForgotPasswordActivity, ToastUtils.PASSWORD_RESET_SUCCESS, Toast.LENGTH_SHORT).show()
                    openIntent(this@ForgotPasswordActivity, LoginActivity::class.java, null, false)
                }
                else {
                    Toast.makeText(this@ForgotPasswordActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}