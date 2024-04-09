package com.example.studyreports

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studyreports.databinding.FragmentFileSettingsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FileSettingsFragment : Fragment() {
    private var _binding: FragmentFileSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFileSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.subject_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.subjectSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.work_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.workTypeSpinner.adapter = adapter
        }

        binding.saveButton.setOnClickListener {
            onSaveButtonClicked()
        }
    }

    private fun onSaveButtonClicked() {
        // Получение выбранных значений и текста из EditText
        val subject = binding.subjectSpinner.selectedItem.toString()
        val workType = binding.workTypeSpinner.selectedItem.toString()
        val workNumber = binding.workNumberEdittext.text.toString()

        // Сформировать итоговое название
        val fileName = "${subject}_${workType}_$workNumber"

        // Предположим, fileUri получен из MainActivity или другого источника
        val fileUri = Uri.parse(arguments?.getString("fileUri"))
        if (fileUri != null) {
            uploadFileToStorage(fileUri, fileName)
        } else {
            Toast.makeText(context, "Ошибка: Файл не выбран", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadFileToStorage(fileUri: Uri, fileName: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("files/$fileName")
        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                // Получаем URL загруженного файла
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Сохраняем URL в Firestore
                    saveFileUrlToFirestore(uri.toString(), fileName)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка загрузки файла: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveFileUrlToFirestore(fileUrl: String, fileName: String) {
        val fileInfo = hashMapOf(
            "url" to fileUrl,
            "fileName" to fileName
        )
        FirebaseFirestore.getInstance().collection("files")
            .add(fileInfo)
            .addOnSuccessListener {
                Toast.makeText(context, "Файл успешно сохранен", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка сохранения ссылки на файл: ${e.message}", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
