package com.example.FishyFinder.Model

data class PredictionResponse(
    val predicted_class: String,
    val confidence: Float
)