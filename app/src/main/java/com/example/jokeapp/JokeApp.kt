package com.example.jokeapp

import android.app.Application

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class JokeApp : Application() {
    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModel = MainViewModel(
            BaseModel(
                retrofit.create(JokeService::class.java),
                ManageResources.Base(this)
            )
        )
    }
}