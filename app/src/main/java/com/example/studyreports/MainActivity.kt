package com.example.studyreports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studyreports.bottom_nav.files.FilesFragment
import com.example.studyreports.bottom_nav.main_profile.MainProfileFragment
import com.example.studyreports.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    companion object {
        private const val PICK_FILE_REQUEST_CODE = 123
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, FilesFragment()).commit()

        binding.mainBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.files -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, FilesFragment()).commit()
                    true
                }

                R.id.add_file -> {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    val mimeTypes = arrayOf(
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    )
                    intent.type = "*/*"
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                    startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
                    true
                }

                R.id.main_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, MainProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileSettingsFragment = FileSettingsFragment()
            val bundle = Bundle()
            data?.data?.let { uri ->
                bundle.putString("fileUri", uri.toString())
            }
            fileSettingsFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, fileSettingsFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
