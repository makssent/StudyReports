package com.example.studyreports

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studyreports.bottom_nav.add_file.AddFileFragment
import com.example.studyreports.bottom_nav.files.FilesFragment
import com.example.studyreports.bottom_nav.main_profile.MainProfileFragment
import com.example.studyreports.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

object FirebaseConstants {
    const val DATABASE_URL = "https://studyreports-bcfce-default-rtdb.europe-west1.firebasedatabase.app"
}
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // FirebaseAuth.getInstance().signOut()
        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, FilesFragment()).commit()
        binding.mainBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.files -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, FilesFragment()).commit()
                    true
                }
                R.id.add_file -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, AddFileFragment()).commit()
                    true
                }
                R.id.main_profile -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MainProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }

    }
}