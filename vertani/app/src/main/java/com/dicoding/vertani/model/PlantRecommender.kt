package com.dicoding.vertani.model

import android.content.Context
import android.util.Log
import com.dicoding.vertani.R
import com.opencsv.CSVReader
import java.io.InputStreamReader

class PlantRecommender(context: Context) {
    private val plantDataset: List<PlantRecommendation>

    init {
        plantDataset = loadPlantDataset(context)
    }

    fun loadPlantDataset(context: Context): List<PlantRecommendation> {
        val inputStream = context.resources.openRawResource(R.raw.new_vegetables_v2)
        val reader = CSVReader(InputStreamReader(inputStream))
        val plants = mutableListOf<PlantRecommendation>()

        reader.readAll().forEachIndexed { index, line ->
            if (index == 0) return@forEachIndexed
            try {
                val name = line[0]
                val description = line[5]
                var minAltitude = line[1].toInt()
                var maxAltitude = line[2].toInt()

                if (minAltitude > maxAltitude) {
                    val temp = minAltitude
                    minAltitude = maxAltitude
                    maxAltitude = temp
                }
                Log.d("PlantRecommender", "Tanaman: $name, minAltitude: $minAltitude, maxAltitude: $maxAltitude, description: $description")
                plants.add(PlantRecommendation(name, description, minAltitude, maxAltitude))
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return plants
    }


    fun recommendPlants(soilPercentage: Float, elevation: List<Float>): List<PlantRecommendation> {
        val singleElevation = elevation.firstOrNull() ?: 0f
        val elevationInt = singleElevation.toInt()
        Log.d("PlantRecommender", "Elevasi: $elevationInt")
        if (elevationInt < 0 || elevationInt > 1000) {
            Log.d("PlantRecommender", "Elevasi tidak valid")
            return emptyList()
        }
        val recommendedPlants = plantDataset.filter {
            it.minAltitude <= elevationInt && it.maxAltitude >= elevationInt
        }
        Log.d("PlantRecommender", "Jumlah tanaman yang cocok: ${recommendedPlants.size}")
        return recommendedPlants.take(4)
    }

}
