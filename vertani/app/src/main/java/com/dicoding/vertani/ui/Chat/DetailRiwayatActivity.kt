package com.dicoding.vertani.ui.Chat

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.vertani.data.entity.UploadHistory
import com.dicoding.vertani.databinding.ActivityDetailRiwayatBinding
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.vertani.R

class DetailRiwayatActivity : AppCompatActivity() {

    private lateinit var tvPrediction: TextView
    private lateinit var tvSoilPercentage: TextView
    private lateinit var tvElevation: TextView
    private lateinit var tvRecommendations: TextView
    private lateinit var binding: ActivityDetailRiwayatBinding
    private lateinit var ivBack: ImageView
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        tvPrediction = binding.tvPrediction
        tvSoilPercentage = binding.tvSoilPercentage
        tvElevation = binding.tvElevation
        tvRecommendations = binding.tvRecommendations
        ivBack = findViewById(R.id.backButton)

        val uploadHistory = intent.getParcelableExtra<UploadHistory>("uploadHistory")

        tvPrediction.text = uploadHistory?.prediction
        tvSoilPercentage.text = uploadHistory?.soilPercentage
        tvElevation.text = uploadHistory?.elevation
        tvRecommendations.text = uploadHistory?.recommendedPlants

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            } else {
                showImage(uploadHistory!!)
            }
        } else {
            showImage(uploadHistory!!)
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val uploadHistory = intent.getParcelableExtra<UploadHistory>("uploadHistory")
                showImage(uploadHistory!!)
            } else {

            }
        }
    }

    private fun showImage(uploadHistory: UploadHistory) {
        val imageUriString = uploadHistory.imageUri
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imageView)
        } else {
            Log.e("DetailRiwayatActivity", "Image URI is null or empty")

            binding.imageView.setImageResource(R.drawable.placeholder_image)
        }
    }
}