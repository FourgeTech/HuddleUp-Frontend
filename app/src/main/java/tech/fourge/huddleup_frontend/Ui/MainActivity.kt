package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.databinding.WelcomePageBinding
import tech.fourge.huddleup_frontend.Utils.openIntent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the LoginActivity
        binding.loginButton.setOnClickListener{
//            openIntent(this, LoginActivity::class.java)
            lifecycleScope.launch {
                val result = UserHelper().signIn("rassie@gmail.com", "Test@123")
                if (result == "success") {
                    openIntent( this@MainActivity, HomeActivity::class.java,null,true)
                }
            }
        }

        // Open the CreateAccountActivity
        binding.createAccountButton.setOnClickListener{
            openIntent(this, CreateAccountActivity::class.java)
        }
    }
}