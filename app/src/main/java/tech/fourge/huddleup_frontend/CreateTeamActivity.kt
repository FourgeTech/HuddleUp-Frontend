package tech.fourge.huddleup_frontend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.fourge.huddleup_frontend.databinding.CreateTeamPageBinding
import tech.fourge.huddleup_frontend.helpers.openIntent

class CreateTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateTeamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnContinue.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }
    }
}