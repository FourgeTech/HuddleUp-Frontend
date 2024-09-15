package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.FirebaseFunctions
import tech.fourge.huddleup_frontend.databinding.CreateTeamPageBinding
import tech.fourge.huddleup_frontend.Utils.openIntent

class CreateTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateTeamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnContinue.setOnClickListener{
            openIntent(this, CreateAccountActivity::class.java)
        }
    }
}