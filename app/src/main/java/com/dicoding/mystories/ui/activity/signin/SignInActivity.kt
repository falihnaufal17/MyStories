package com.dicoding.mystories.ui.activity.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystories.data.local.UserPreference
import com.dicoding.mystories.data.retrofit.ApiConfig
import com.dicoding.mystories.databinding.ActivitySignInBinding
import com.dicoding.mystories.model.UserModel
import com.dicoding.mystories.repository.UsersRepository
import com.dicoding.mystories.ui.activity.home.HomeActivity
import com.dicoding.mystories.ui.activity.signup.SignUpActivity
import com.dicoding.mystories.utils.LoginViewModelFactory

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var usersRepository: UsersRepository
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userModel = UserModel()

        usersRepository = UsersRepository(ApiConfig.getApiService(""))
        signInViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(usersRepository)
        )[SignInViewModel::class.java]

        binding.btnSignIn.setOnClickListener {
            val edEmail = binding.edLoginEmail.text
            val edPassword = binding.edLoginPassword.text

            signInViewModel.postLogin(edEmail.toString(), edPassword.toString())
        }

        binding.linkSignUp.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        signInViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.btnSignIn.isEnabled = false
                binding.linkSignUp.isEnabled = false
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.btnSignIn.isEnabled = true
                binding.linkSignUp.isEnabled = true
                binding.loading.visibility = View.INVISIBLE
            }
        }

        signInViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        signInViewModel.loginResult.observe(this) {
            val userPreference = UserPreference(this)
            userModel.name = it?.name
            userModel.userId = it?.userId
            userModel.token = it?.token

            userPreference.setUser(userModel)

            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finish()
        }
    }
}