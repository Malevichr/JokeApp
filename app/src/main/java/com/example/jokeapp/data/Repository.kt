package com.example.jokeapp.data

import com.example.jokeapp.presentation.JokeUi


interface Repository<S, E> {
    fun fetch()
    fun init(callback: ResultCallback<S, E>)
    fun clear()
    fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>)
    fun chooseFavorites(favorite: Boolean)
}
interface ResultCallback<S, E>{

    fun provideSuccess(data: S)

    fun provideError(error: E)
}