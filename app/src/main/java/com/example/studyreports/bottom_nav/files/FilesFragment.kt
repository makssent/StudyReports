package com.example.studyreports.bottom_nav.files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyreports.databinding.FragmentFilesBinding


class FilesFragment : Fragment() {
    private lateinit var binding: FragmentFilesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }
}