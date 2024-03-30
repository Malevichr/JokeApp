package com.example.jokeapp.data

interface DataSource{
    suspend fun fetch(): JokeResult
}