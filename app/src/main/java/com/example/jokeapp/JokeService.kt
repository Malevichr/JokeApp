package com.example.jokeapp


import retrofit2.Call
import retrofit2.http.GET


interface JokeService {
    @GET("jokes/random")
    fun joke(): Call<JokeCloud>
}