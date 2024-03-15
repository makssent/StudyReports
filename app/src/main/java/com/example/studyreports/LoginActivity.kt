package com.example.studyreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.studyreports.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAuth.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Authentication successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                            binding.editTextPassword.text.clear()
                        }
                    }
            }
        }
        binding.textResetpassword.setOnClickListener{
            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }
        binding.textCreatepassword.setOnClickListener{
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
    }
}