package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.JoinTeamPageBinding
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding

class JoinTeamActivity : AppCompatActivity() {
    private lateinit var binding: JoinTeamPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = JoinTeamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the CreateAccountActivity
        binding.continueButton.setOnClickListener{
            openIntent(this, CreateAccountActivity::class.java)
        }
    }
}