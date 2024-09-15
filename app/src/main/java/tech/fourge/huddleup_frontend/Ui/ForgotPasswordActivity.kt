package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.ForgotPasswordPageBinding
import tech.fourge.huddleup_frontend.Helpers.UserHelper

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ForgotPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetPasswordButton.setOnClickListener{
            lifecycleScope.launch { UserHelper().resetPassword(binding.emailEditText.text.toString()) }
        }
    }
}