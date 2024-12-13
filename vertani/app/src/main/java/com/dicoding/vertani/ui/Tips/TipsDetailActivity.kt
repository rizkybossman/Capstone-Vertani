package com.dicoding.vertani.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.vertani.R
import com.dicoding.vertani.model.FarmingTip

class TipsDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tips_activity)
        supportActionBar?.hide()
        val backButton: ImageView = findViewById(R.id.backButton)
        val imageIllustration: ImageView = findViewById(R.id.imageIllustration)
        val tipsTitle: TextView = findViewById(R.id.tvTitle)
        val tipsContent: TextView = findViewById(R.id.tvDescription)


        val farmingTip = intent.getParcelableExtra<FarmingTip>("FARMING_TIP")

        farmingTip?.let {
            tipsTitle.text = it.title
            tipsContent.text = it.description


            Glide.with(this)
                .load(it.imageUrl ?: R.drawable.image1)
                .into(imageIllustration)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
