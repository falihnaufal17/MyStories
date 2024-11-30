package com.dicoding.mystories.ui.activity.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystories.data.retrofit.ApiConfig
import com.dicoding.mystories.databinding.ActivitySignUpBinding
import com.dicoding.mystories.repository.UsersRepository
import com.dicoding.mystories.utils.RegisterViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var usersRepository: UsersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        usersRepository = UsersRepository(ApiConfig.getApiService(""))
        signUpViewModel = ViewModelProvider(this, RegisterViewModelFactory(usersRepository))[SignUpViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            val edName = binding.edRegisterName.text
            val edEmail = binding.edRegisterEmail.text
            val edPassword = binding.edRegisterPassword.text

            signUpViewModel.postRegister(edName.toString(), edEmail.toString(),
                edPassword.toString()
            )
        }

        signUpViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        signUpViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.btnSignUp.isEnabled = false
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.btnSignUp.isEnabled = true
                binding.loading.visibility = View.INVISIBLE
            }
        }
    }
}