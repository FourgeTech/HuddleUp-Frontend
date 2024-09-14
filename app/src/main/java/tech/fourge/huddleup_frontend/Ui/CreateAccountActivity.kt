package tech.fourge.huddleup_frontend.Ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.databinding.CreateAccountBinding


class CreateAccountActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateAccountBinding.inflate(layoutInflater)
        lateinit var googleSignInClient: GoogleSignInClient
        setContentView(binding.root)

        binding.continueButton.setOnClickListener{
        }

        binding.continueWithGoogleButton.setOnClickListener{
        }
    }
}
