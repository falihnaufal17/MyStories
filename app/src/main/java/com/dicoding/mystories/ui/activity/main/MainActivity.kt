package com.dicoding.mystories.ui.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystories.data.local.UserPreference
import com.dicoding.mystories.databinding.ActivityMainBinding
import com.dicoding.mystories.ui.activity.home.HomeActivity
import com.dicoding.mystories.ui.activity.signin.SignInActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            checkToken()
        }
    }

    private fun checkToken() {
        val userPreference = UserPreference(this)
        val token = userPreference.getUser().token

        if (token != null) {
            if (token.isEmpty()) {
                val signInIntent = Intent(this, SignInActivity::class.java)
                startActivity(signInIntent)
            } else {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }
        }

    }
}