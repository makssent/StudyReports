package com.example.studyreports.bottom_nav.main_profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyreports.LoginActivity
import com.example.studyreports.R
import com.example.studyreports.bottom_nav.main_profile.personalinformation.PersonalInformationFragment
import com.example.studyreports.databinding.FragmentMainProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainProfileFragment : Fragment() {
    private lateinit var binding: FragmentMainProfileBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false)

        if (FirebaseAuth.getInstance().currentUser != null) {
            binding.mainProfileTextViewEmail.text = FirebaseAuth.getInstance().currentUser?.email
            // Запрашиваем username пользователя из Firestore
            FirebaseAuth.getInstance().currentUser?.let {
                FirebaseFirestore.getInstance().collection("Users").document(it.uid).get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Если документ существует, извлекаем username и устанавливаем его
                        binding.mainProfileTextViewUsername.text = document.getString("username")
                    } else {
                        binding.mainProfileTextViewUsername.setText(R.string.text_en_text_findUserFalse)
                    }
                }
            }
        }
        binding.mainProfileButtonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.mainProfileButtonPersonalInformation.setOnClickListener {

            // Получение FragmentManager для начала транзакции
            // Используется parentFragmentManager для получения менеджера фрагментов, ассоциированного с активностью
            val transaction = parentFragmentManager.beginTransaction()

            // Замена фрагмента в контейнере (R.id.fragment_container) на PersonalInformationFragment
            // Убедитесь, что R.id.fragment_container это ID контейнера в layout вашей MainActivity, где должны отображаться фрагменты
            transaction.replace(R.id.fragmentContainer, PersonalInformationFragment())
            // Добавление транзакции в back stack, чтобы позволить пользователю возвращаться назад к предыдущему фрагменту через системную кнопку Назад
            transaction.addToBackStack(null)
            // Подтверждение транзакции
            transaction.commit()
        }

        return binding.root
        }
    }
