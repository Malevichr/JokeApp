package com.example.jokeapp.data


interface Repository<S, E> {
    fun fetch() : JokeResult
    fun changeJokeStatus(): JokeResult
    fun chooseFavorites(favorite: Boolean)
}
