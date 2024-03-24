package com.example.jokeapp.data.cloud


import retrofit2.Call
import retrofit2.http.GET


interface JokeService {
    @GET("jokes/random")
    fun joke(): Call<JokeCloud>
}