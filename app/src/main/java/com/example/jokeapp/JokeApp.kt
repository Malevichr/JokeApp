package com.example.jokeapp

import android.app.Application
import android.util.Log
import com.example.jokeapp.data.BaseRepository
import com.example.jokeapp.data.FakeRepository
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.data.cloud.cache.CacheDataSource
import com.example.jokeapp.data.cloud.cache.JokeCache
import com.example.jokeapp.presentation.MainViewModel
import com.example.jokeapp.presentation.ManageResources
import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

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

        val realmConfiguration = RealmConfiguration.create(schema = setOf(JokeCache::class))

        val realm = Realm.open(realmConfiguration)

        val results = realm.query<JokeCache>().find()

        for (jokeCache in results)
            Log.d("Mlvch", jokeCache.id + " " + jokeCache.text)
        realm.close()


        viewModel = MainViewModel(
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(JokeService::class.java),
                    manageResources
                ),
                CacheDataSource.Base(realmConfiguration, manageResources),
                manageResources
            )
        )
    }
}