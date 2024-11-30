package com.dicoding.mystories.ui.activity.addstory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystories.R
import com.dicoding.mystories.data.local.UserPreference
import com.dicoding.mystories.databinding.ActivityAddStoryBinding
import com.dicoding.mystories.ui.activity.home.HomeActivity
import com.dicoding.mystories.ui.activity.signin.SignInActivity
import com.dicoding.mystories.utils.FileHandler
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        AddStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)

        val userPreference = UserPreference(this)
        val token = userPreference.getUser().token
        if (token == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        setContentView(binding.root)

        binding.btnAddStoryGallery.setOnClickListener {
            startGallery()
        }

        binding.btnAddStoryCamera.setOnClickListener {
            startCamera()
        }

        binding.btnAddStorySubmit.setOnClickListener {
            uploadImage()
        }

        addStoryViewModel.message.observe(this) {
            if (it != null) {
                showToast(it)
            }

            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finish()
        }

        addStoryViewModel.isLoading.observe(this) {
            if (it != null) {
                showLoading(it)
            }
        }

        addStoryViewModel.preview.observe(this) {
            if (it != null) {
                showImage(it)
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        addStoryViewModel.savePreview(FileHandler().getImageUri(this))
        launcherIntentCamera.launch(FileHandler().getImageUri(this))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            addStoryViewModel.savePreview(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            addStoryViewModel.preview.value.let {
                if (it != null) {
                    showImage(it)
                }
            }
        } else {
            addStoryViewModel.savePreview(null)
        }
    }

    private fun uploadImage() {
        addStoryViewModel.preview.value?.let { uri ->
            val imageFile = FileHandler().uriToFile(uri, this)
            val description = binding.edAddStoryMultiline.text.toString()
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.postStory(requestBody, multipartBody)

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnAddStorySubmit.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage(imageUri: Uri) {
        Log.d("Image URI", "showImage: $imageUri")
        binding.imgPreview.setImageURI(imageUri)
    }
}