package com.example.jokeapp.data


interface Repository<S, E> {
    suspend fun fetch() : JokeResult
    suspend fun changeJokeStatus(): JokeResult
    fun chooseFavorites(favorite: Boolean)
}
