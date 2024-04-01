package com.example.studyreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.studyreports.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
                            val user = hashMapOf(
                                "email" to email,
                                "username" to username,
                                "name" to "",
                                "surname" to "",
                                "option" to 0
                            )
                            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(applicationContext, "Failed to register user: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
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