package tech.fourge.huddleup_frontend.Ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.databinding.LoginPageBinding
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Ui.ForgotPasswordActivity
import tech.fourge.huddleup_frontend.Ui.GoogleAuthActivity
import tech.fourge.huddleup_frontend.Ui.HomeActivity
import tech.fourge.huddleup_frontend.Utils.ToastUtils
import tech.fourge.huddleup_frontend.Utils.openIntent

class LoginActivity : AppCompatActivity() {

    private lateinit var enrollLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: LoginPageBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBiometricAuthentication()
        setupEnrollLauncher()

        binding.buttonBioLogin.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                // If biometrics aren't set up, prompt the user to enroll
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    }
                    enrollLauncher.launch(enrollIntent)
                }
            }
        }

        // Sign in the user
        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, ToastUtils.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val result = UserHelper().signIn(email, password)
                    if (result == "success") {
                        Toast.makeText(this@LoginActivity, ToastUtils.SIGN_IN_SUCCESS, Toast.LENGTH_SHORT).show()
                        openIntent(this@LoginActivity, HomeActivity::class.java, null, true)
                    } else {
                        Toast.makeText(this@LoginActivity, result, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Continue with Google single sign-on
        binding.continueWithGoogleButton.setOnClickListener {
            // Pass data to the GoogleAuthActivity
            val data = Bundle().apply {
                putString("action", "login")
            }
            openIntent(this, GoogleAuthActivity::class.java,data)
        }


        // Open the CreateAccountActivity
        binding.resetPasswordButton.setOnClickListener{
            openIntent(this, ForgotPasswordActivity::class.java)
        }
    }

    private fun setupBiometricAuthentication() {
        biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setDescription("Log in using your biometric credential")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    private fun setupEnrollLauncher() {
        enrollLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Handle enroll activity result if needed
            println("Enroll result: $it")
        }
    }
}
