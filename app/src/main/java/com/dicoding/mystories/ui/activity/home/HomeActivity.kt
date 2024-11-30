package com.dicoding.mystories.ui.activity.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystories.R
import com.dicoding.mystories.adapter.LoadingStateAdapter
import com.dicoding.mystories.adapter.StoryListAdapter
import com.dicoding.mystories.data.local.UserPreference
import com.dicoding.mystories.databinding.ActivityHomeBinding
import com.dicoding.mystories.model.UserModel
import com.dicoding.mystories.ui.activity.addstory.AddStoryActivity
import com.dicoding.mystories.ui.activity.detail.DetailActivity
import com.dicoding.mystories.ui.activity.main.MainActivity
import com.dicoding.mystories.ui.activity.mapsactivity.MapsActivity
import com.dicoding.mystories.ui.activity.signin.SignInActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userModel: UserModel
    private val homeActivityViewModel: HomeActivityViewModel by viewModels {
        HomeViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userModel = UserModel()

        val userPreference = UserPreference(this)
        val token = userPreference.getUser().token

        if (token == null) {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_sign_out -> {
                    onSignOut()
                    true
                }
                R.id.btn_map -> {
                    onOpenMap()
                    true
                }

                else -> false
            }
        }

        binding.btnAddStory.setOnClickListener {
            onRedirectToAddStory()
        }

        setStoryData()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStorylist.layoutManager = layoutManager
    }

    private fun onSignOut() {
        val userPreference = UserPreference(this)

        userModel.userId = null
        userModel.name = null
        userModel.token = null

        userPreference.setUser(userModel)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onOpenMap() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private fun onRedirectToAddStory() {
        val addStoryIntent = Intent(this, AddStoryActivity::class.java)
        startActivity(addStoryIntent)
    }

    private fun setStoryData() {
        val adapter = StoryListAdapter()

        binding.rvStorylist.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        homeActivityViewModel.listStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(image: View, name: View, desc: View, id: String) {
                onSelectedItem(image, name, desc, id)
            }
        })
    }

    private fun onSelectedItem(image: View, name: View, desc: View, id: String) {
        val intentDetail = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID, id)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(image, image.transitionName),
            Pair(name, name.transitionName),
            Pair(desc, desc.transitionName)
        )
        startActivity(intentDetail, options.toBundle())
    }
}