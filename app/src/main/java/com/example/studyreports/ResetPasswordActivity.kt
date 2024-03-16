package com.example.studyreports

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studyreports.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            val email = binding.resetEmailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "If there's an account associated with this email, we've sent a password reset link to it", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        binding.resetCreateText.setOnClickListener{
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
    }
}
