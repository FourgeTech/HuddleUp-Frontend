package tech.fourge.huddleup_frontend.Ui

import tech.fourge.huddleup_frontend.Ui.LoginActivity
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
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
//            lifecycleScope.launch {
//                val result = UserHelper().signIn("rassie@gmail.com", "Test@123")
//                if (result == "success") {
//                    openIntent( this@MainActivity, HomeActivity::class.java,null,true)
//                }
//            }
        }

        // Open the CreateAccountActivity
        binding.createAccountButton.setOnClickListener{
            openIntent(this, CreateAccountActivity::class.java)
        }
    }
}