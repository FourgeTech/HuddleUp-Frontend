package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.HomePageBinding
import tech.fourge.huddleup_frontend.databinding.InvitePlayersPageBinding
import tech.fourge.huddleup_frontend.databinding.JoinTeamPageBinding

class InvitePlayerActivity : AppCompatActivity() {
    private lateinit var binding: InvitePlayersPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InvitePlayersPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val teamCode = intent.getStringExtra("teamCode") ?: ""
        Log.d("TeamCode", teamCode)
        binding.teamCode.text = teamCode

        val currentUserUtil = CurrentUserUtil
        binding.loginButton.setOnClickListener {
            currentUserUtil.currentUser.role = "manager"
            openIntent(this, HomeActivity::class.java, null, true)
        }
    }
}