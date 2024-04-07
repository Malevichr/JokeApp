package com.example.jokeapp.data

interface DataSource{
    suspend fun fetch(language: Language): JokeResult
}