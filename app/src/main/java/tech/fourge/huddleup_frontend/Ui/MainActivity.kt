package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding
import tech.fourge.huddleup_frontend.Utils.openIntent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the LoginActivity
        binding.loginButton.setOnClickListener{
            openIntent(this, LoginActivity::class.java)
        }

        // Open the CreateTeamActivity
        binding.createTeamButton.setOnClickListener{
            openIntent(this, CreateTeamActivity::class.java)
        }

        // Open the JoinTeamActivity
        binding.joinTeamButton.setOnClickListener{
            openIntent(this, JoinTeamActivity::class.java)
        }
    }
}