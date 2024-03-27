package com.example.studyreports.bottom_nav.main_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyreports.databinding.FragmentMainProfileBinding


class MainProfileFragment : Fragment() {
    private lateinit var binding: FragmentMainProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}