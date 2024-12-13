package com.dicoding.vertani.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.vertani.R
import androidx.lifecycle.lifecycleScope
import com.dicoding.vertani.data.entity.UploadHistory
import com.dicoding.vertani.data.response.ErrorResponse
import com.dicoding.vertani.data.retrofit.ApiConfigg
import com.dicoding.vertani.data.room.UploadHistoryDao
import com.dicoding.vertani.data.room.UploadHistoryDatabase
import com.dicoding.vertani.databinding.ChatActivityBinding
import com.dicoding.vertani.getImageUri
import com.dicoding.vertani.model.PlantRecommendation
import com.dicoding.vertani.model.PlantRecommender
import kotlinx.coroutines.launch
import com.dicoding.vertani.reduceFileImage
import com.dicoding.vertani.service.ElevationService
import com.dicoding.vertani.uriToFile
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ChatActivityBinding
    private lateinit var plantRecommender: PlantRecommender
    private val elevationService = ElevationService()
    private var currentImageUri: Uri? = null
    private lateinit var uploadHistoryDao: UploadHistoryDao
    private lateinit var uploadHistoryDatabase: UploadHistoryDatabase
    private lateinit var ivBack: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        plantRecommender = PlantRecommender(this)
        uploadHistoryDatabase = UploadHistoryDatabase.getInstance(this, FirebaseAuth.getInstance().currentUser!!.uid)
        uploadHistoryDao = uploadHistoryDatabase.uploadHistoryDao()
        ivBack = findViewById(R.id.backButton)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image Classification File", "showImage: ${imageFile.path}")
            showLoading(true)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfigg.getApiService()
                    val successResponse = apiService.uploadImage(multipartBody)


                    val elevationList = elevationService.fetchElevation(-7.023744444444445, 107.54787777777777) // Mendapatkan list ketinggian
                    val recommendations = plantRecommender.recommendPlants(0f, elevationList)

                    with(successResponse) {
                        binding.edAddDescription.text = "Prediction: ${prediction ?: "N/A"}\n" + "soil_percentage: ${soilPercentage ?: "N/A"}\n"
                        displayResults(0f, elevationList, recommendations)

                        val uploadHistory = UploadHistory(
                            id = null,
                            userId = FirebaseAuth.getInstance().currentUser?.uid,
                            prediction = prediction ?: "N/A",
                            soilPercentage = soilPercentage ?: "N/A",
                            elevation = elevationService.fetchElevation(-7.023744444444445, 107.54787777777777).toString(),
                            recommendedPlants = recommendations.toString(),
                            imageUri = uri.toString()
                        )
                        uploadHistoryDao.insert(uploadHistory)
                    }
                    showLoading(false)

                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    showToast(errorResponse.message ?: "An error occurred")
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayResults(soilPercentage: Float, elevation: List<Float>, recommendations: List<PlantRecommendation>) {
        val textResult = findViewById<TextView>(R.id.ed_recomendation)

        val result = StringBuilder().apply {
            append("Elevation: $elevation meters\n")
            append("Recommended Plants:\n")
            recommendations.forEach {
                append("- ${it.name}: ${it.description}\n")
            }
        }

        textResult.text = result.toString()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}


