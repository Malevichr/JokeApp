package com.example.jokeapp.data

import java.lang.IllegalStateException

interface  JokeResult : Joke{
    fun isFavorite(): Boolean
    fun isSuccessful(): Boolean
    fun errorMessage(): String
    class Success(private val joke: Joke, private val toFavorite: Boolean, val language: Language) : JokeResult {
        override fun isFavorite(): Boolean = toFavorite

        override fun isSuccessful() = true

        override fun errorMessage(): String = ""
        override suspend fun <T> map(mapper: Joke.Mapper<T>): T = joke.map(mapper)
    }

    class Failure(private val error: Error) : JokeResult {
        override fun isFavorite(): Boolean = false
        override fun isSuccessful() = false
        override fun errorMessage(): String = error.message()
        override suspend fun <T> map(mapper: Joke.Mapper<T>): T = throw IllegalStateException()
    }
}
