package com.example.studyreports.bottom_nav.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studyreports.CreatorProfileFragment
import com.example.studyreports.R
import com.example.studyreports.databinding.FragmentFilesBinding
import com.google.firebase.firestore.FirebaseFirestore

class FilesFragment : Fragment() {
    private lateinit var binding: FragmentFilesBinding
    private lateinit var filesListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filesListView = binding.filesListView
        getFilesFromFirestore()
        filesListView.setOnItemClickListener { _, _, _, _ ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.LinearLayout, CreatorProfileFragment())
                .addToBackStack(null)
                .commit()
            binding.filesSearchView.visibility = View.GONE
        }
    }

    private fun getFilesFromFirestore() {
        FirebaseFirestore.getInstance().collection("files")
            .get()
            .addOnSuccessListener { documents ->
                val filesList = mutableListOf<String>()
                for (document in documents) {
                    val fileName = document.getString("fileName")
                    if (fileName != null) {
                        filesList.add(fileName)
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filesList)
                filesListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Ошибка загрузки файлов: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
