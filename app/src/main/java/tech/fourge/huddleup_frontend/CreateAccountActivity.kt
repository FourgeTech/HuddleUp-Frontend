package tech.fourge.huddleup_frontend

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.databinding.CreateAccountBinding


class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
