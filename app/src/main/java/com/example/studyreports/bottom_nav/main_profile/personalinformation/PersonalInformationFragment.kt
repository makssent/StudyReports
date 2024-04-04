package com.example.studyreports.bottom_nav.main_profile.personalinformation

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.studyreports.databinding.FragmentPersonalInformationBinding
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class PersonalInformationFragment : Fragment() {
    private lateinit var binding: FragmentPersonalInformationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPersonalInformationBinding.inflate(inflater, container, false)
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser?.let {
                FirebaseFirestore.getInstance().collection("Users").document(it.uid).get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        if (document.getString("name") != "" && document.getString("surname") != ""){
                            binding.personalEditTextRealName.setText(document.getString("name"))
                            binding.personalEditTextRealSurname.setText(document.getString("surname"))
                            binding.personalEditTextRealName.isEnabled = false
                            binding.personalEditTextRealSurname.isEnabled = false
                            binding.personalEditTextRealSurname.setTextColor(Color.parseColor("#3F51B5"))
                            binding.personalEditTextRealName.setTextColor(Color.parseColor("#3F51B5"))

                        }
                        else{
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Information")
                            builder.setMessage("Поля ввода имени и фамилии нельзя будет изменить в дальнейшем, вы понимаете это?")
                            builder.setPositiveButton("Да") { _, _ -> }
                            builder.create().show()
                        }
                    }
                }
            }
        }
        binding.personalInformationButtonSave.setOnClickListener{
            val option =  binding.personalEditTextOption.text.toString().trim()
            if(binding.personalEditTextRealSurname.isEnabled && binding.personalEditTextRealName.isEnabled){
                val name = binding.personalEditTextRealName.text.toString().trim()
                val surname = binding.personalEditTextRealSurname.text.toString().trim()
                if(name.isNotEmpty() && surname.isNotEmpty() && option.isNotEmpty()) {
                    val optionNumber = option.toIntOrNull()

                    if (optionNumber != null && optionNumber > 0) {
                        if(name.matches(Regex("^[а-яА-ЯёЁ]+$")) && surname.matches(Regex("^[а-яА-ЯёЁ]+$"))){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Подтверджение")
                            builder.setMessage("Вы ввели правильное ФИ?" + "\nИмя: " + name + "\nФамилия: " + surname)
                            builder.setPositiveButton("Да") { dialog, which ->
                                val user = hashMapOf(
                                    "name" to name,
                                    "surname" to surname,
                                    "option" to optionNumber
                                )
                                FirebaseFirestore.getInstance().collection("Users")
                                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .set(user, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                                        binding.personalEditTextRealName.isEnabled = false
                                        binding.personalEditTextRealSurname.isEnabled = false
                                        binding.personalEditTextOption.text.clear()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "Ошибка при обновлении данных: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            builder.setNegativeButton("Нет") { dialog, which ->
                                dialog.dismiss()
                            }
                            builder.create().show()
                        }
                        else{
                            Toast.makeText(requireContext(), "ФИ должны состоять только из русских символов!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Вариант должен быть больше нуля!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val optionNumber = option.toIntOrNull()

                if (optionNumber != null && optionNumber > 0) {
                    val user = hashMapOf(
                        "option" to optionNumber
                    )
                    FirebaseFirestore.getInstance().collection("Users")
                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                            binding.personalEditTextOption.text.clear()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Ошибка при обновлении данных: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    Toast.makeText(requireContext(), "Вариант должен быть больше 0!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding.root
    }
}