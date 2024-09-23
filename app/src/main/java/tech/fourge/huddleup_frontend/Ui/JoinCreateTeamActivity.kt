package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.JoinCreateTeamActivityBinding

class JoinCreateTeamActivity : AppCompatActivity() {
    private lateinit var binding: JoinCreateTeamActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = JoinCreateTeamActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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