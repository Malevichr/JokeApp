package com.example.jokeapp.data

import java.lang.IllegalStateException

interface  JokeResult : Joke{
    fun isFavorite(): Boolean
    fun isSuccessful(): Boolean
    fun errorMessage(): String
    class Success(val joke: Joke, private val toFavorite: Boolean) : JokeResult {
        override fun isFavorite(): Boolean = toFavorite

        override fun isSuccessful() = true

        override fun errorMessage(): String = ""
        override fun <T> map(mapper: Joke.Mapper<T>): T = joke.map(mapper)
    }

    class Failure(val error: Error) : JokeResult {
        override fun isFavorite(): Boolean = false
        override fun isSuccessful() = false
        override fun errorMessage(): String = error.message()
        override fun <T> map(mapper: Joke.Mapper<T>): T = throw IllegalStateException()
    }
}
