package tech.fourge.huddleup_frontend.Ui

import tech.fourge.huddleup_frontend.Ui.LoginActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding
import tech.fourge.huddleup_frontend.Utils.openIntent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the LoginActivity
        binding.loginButton.setOnClickListener{
            openIntent(this, LoginActivity::class.java)
        }

        // Open the CreateAccountActivity
        binding.createAccountButton.setOnClickListener{
            openIntent(this, CreateAccountActivity::class.java)
        }
    }
}