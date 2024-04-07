package com.example.jokeapp.data


interface Repository<S, E> {
    suspend fun fetch() : JokeResult
    suspend fun changeJokeStatus(): JokeResult
    suspend fun changeLanguage(language: Language): JokeResult
    fun chooseFavorites(favorite: Boolean)
}
