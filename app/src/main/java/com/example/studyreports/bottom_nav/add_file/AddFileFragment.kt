package com.example.studyreports.bottom_nav.add_file

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyreports.databinding.FragmentAddFileBinding


class AddFileFragment : Fragment() {
    private lateinit var binding: FragmentAddFileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddFileBinding.inflate(inflater, container, false)
        return binding.root
    }
}
