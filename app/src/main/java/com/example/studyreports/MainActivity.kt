package com.example.studyreports

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

object FirebaseConstants {
    const val DATABASE_URL = "https://studyreports-bcfce-default-rtdb.europe-west1.firebasedatabase.app"
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseAuth.getInstance().signOut()
        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}