package com.dicoding.mystories.ui.activity.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.mystories.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_ID)

        binding.apply {
            imageView2.transitionName = "story_image_$storyId"
            name.transitionName = "story_name_$storyId"
            description.transitionName = "story_description_$storyId"
        }

        if (storyId != null) {
            detailViewModel.getDetailStory(storyId)
        }

        detailViewModel.detail.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it.photoUrl)
                    .into(binding.imageView2)
                binding.name.text = it.name
                binding.description.text = it.description
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}