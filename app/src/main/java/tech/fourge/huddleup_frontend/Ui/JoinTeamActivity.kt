package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.TeamHelper
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.JoinTeamPageBinding


class JoinTeamActivity : AppCompatActivity() {
    private lateinit var binding: JoinTeamPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = JoinTeamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)





        // Open the Home Page
        binding.continueButton.setOnClickListener{
            val teamCode = binding.teamCode.text.toString().trim()

            if (teamCode.isEmpty()) {
                binding.teamCode.error = "Please enter a valid team code"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val result = TeamHelper().joinTeamByCode(teamCode)
                if (result == "success") {
                    Toast.makeText(this@JoinTeamActivity, "Successfully joined the team!", Toast.LENGTH_SHORT).show()
                    openIntent(this@JoinTeamActivity, HomeActivity::class.java, null, true)
                } else {
                    Toast.makeText(this@JoinTeamActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}