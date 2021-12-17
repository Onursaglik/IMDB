package com.example.imdb.network

import com.example.imdb.model.ApiResponse
import com.example.imdb.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie?")
    suspend fun getPopularSeries(@Query("api_key") api_key: String = "***",
                                 @Query("sort_by") sort_by: String = "popularity.desc",
                                 @Query("page") page: Int): ApiResponse

    @GET("search/movie?")
    suspend fun getSearchSeries(@Query("api_key") api_key: String = "***",
                                @Query("query") query: String,
                                @Query("page") page: Int): ApiResponse

    @GET("movie/{id}?")
    suspend fun getMovieDetail(
            @Path("id") id: Int?,
            @Query("api_key") api_key: String = "***",
    ): Movie

}
