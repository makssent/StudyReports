package com.example.studyreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.studyreports.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationButtonCreate.setOnClickListener{
            val email = binding.registrationEmailEditText.text.toString().trim()
            val password = binding.registrationPasswordEditText.text.toString().trim()
            val username = binding.registrationUsernameEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userInfo = hashMapOf<String, String>()
                            userInfo["email"] = email
                            userInfo["username"] = username
                            FirebaseDatabase.getInstance(FirebaseConstants.DATABASE_URL).reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(userInfo)
                            startActivity(Intent(this,MainActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.registrationLoginText.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}