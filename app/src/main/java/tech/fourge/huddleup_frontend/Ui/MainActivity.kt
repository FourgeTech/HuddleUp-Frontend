package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding
import tech.fourge.huddleup_frontend.helpers.openIntent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener{
            openIntent(this, LoginActivity::class.java)
        }
        binding.createTeamButton.setOnClickListener{
            openIntent(this, CreateTeamActivity::class.java)
        }
    }
}