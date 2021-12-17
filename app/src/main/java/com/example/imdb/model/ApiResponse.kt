package com.example.imdb.model;

import com.google.gson.annotations.SerializedName;

data class ApiResponse(
        @SerializedName("results") val results: List<Movie>
)