package tech.fourge.huddleup_frontend.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.CreateTeamPageBinding
import tech.fourge.huddleup_frontend.Utils.ValidationUtils
import tech.fourge.huddleup_frontend.Helpers.TeamHelper
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.openIntent

class CreateTeamActivity : AppCompatActivity() {
    // Initialize variables
    lateinit var binding: CreateTeamPageBinding
    lateinit var teamName: String
    lateinit var location: String
    lateinit var league: String
    val validationUtils = ValidationUtils()

    // OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateTeamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Continue button click
        binding.btnContinue.setOnClickListener {

            // Check if form submission is valid
            if (!handleFormSubmission()) {
                return@setOnClickListener
            }

            // If the form is valid, proceed with team creation
            lifecycleScope.launch {
                val result = TeamHelper().registerTeam(teamName, location, league)
                if (result != "unknown_error") {
                    UserHelper().getUser(CurrentUserUtil.currentUserUID)
                    val intent = Intent(this@CreateTeamActivity, InvitePlayerActivity::class.java)
                    Toast.makeText(this@CreateTeamActivity, ToastUtils.TEAM_CREATION_SUCCESS, Toast.LENGTH_SHORT).show()
                    intent.putExtra("teamCode", result)
                    startActivity(intent)
                    Log.d("TeamCode", result)
                } else {
                    Toast.makeText(this@CreateTeamActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Handle form validation
    private fun handleFormSubmission(): Boolean {
        teamName = binding.teamName.text.toString().trim()
        location = binding.teamLocation.text.toString().trim()
        league = binding.teamLeague.text.toString().trim()

        // Validate inputs
        if (!validationUtils.isTeamNameValid(teamName)) {
            binding.teamName.error = "Please enter a valid team name"
            return false
        }

        if (!validationUtils.isTeamLocationValid(location)) {
            binding.teamLocation.error = "Please enter a valid location"
            return false
        }

        if (!validationUtils.isTeamLeagueValid(league)) {
            binding.teamLeague.error = "Please enter a valid league"
            return false
        }

        return true
    }
}
