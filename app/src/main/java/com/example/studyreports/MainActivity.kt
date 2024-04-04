package com.example.studyreports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studyreports.bottom_nav.files.FilesFragment
import com.example.studyreports.bottom_nav.main_profile.MainProfileFragment
import com.example.studyreports.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

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
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    // Указание нескольких MIME типов для выбора файлов Word (.doc и .docx)
                    val mimeTypes = arrayOf(
                        "application/msword", // Для .doc
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // Для .docx
                    )
                    intent.type = "*/*" // Этот тип должен быть установлен, даже если вы используете EXTRA_MIME_TYPES
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                    startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
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
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Получаем путь к файлу и его название для сохранения в Firestore
                val fileName = uri.lastPathSegment ?: "unknown_file_${UUID.randomUUID()}"
                val storageReference =
                    FirebaseStorage.getInstance().getReference("uploads/$fileName")

                storageReference.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            val fileInfo = hashMapOf(
                                "fileName" to fileName,
                                "fileUrl" to downloadUri.toString(),
                            )

                            FirebaseFirestore.getInstance().collection("files")
                                .add(fileInfo)
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Файл успешно добавлен!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(applicationContext, "Ошибка, файл не добавлен!", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        // Обработка ошибки загрузки файла
                    }
            }
        }
    }
}
