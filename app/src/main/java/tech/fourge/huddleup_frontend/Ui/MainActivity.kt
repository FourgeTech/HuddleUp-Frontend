package tech.fourge.huddleup_frontend.Ui

import tech.fourge.huddleup_frontend.Ui.LoginActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding
import tech.fourge.huddleup_frontend.Utils.openIntent
import com.google.firebase.messaging.FirebaseMessaging
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed: ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("MainActivity","FCM Token: $token")
            CurrentUserUtil.fcmToken = token
        }

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