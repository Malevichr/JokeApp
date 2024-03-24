package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.BaseRepository
import com.example.jokeapp.data.FakeRepository
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.data.cloud.cache.CacheDataSource
import com.example.jokeapp.presentation.MainViewModel
import com.example.jokeapp.presentation.ManageResources

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
        val manageResources = ManageResources.Base(this)
        viewModel = MainViewModel(
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(JokeService::class.java),
                    manageResources
                ),
                CacheDataSource.Fake(manageResources),
                manageResources
            )
        )
    }
}