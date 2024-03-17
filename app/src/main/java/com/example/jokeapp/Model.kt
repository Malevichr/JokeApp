package com.example.jokeapp


interface Model<S, E> {
    fun fetch()
    fun init(callback: ResultCallback<S, E>)
    fun clear()
}
interface ResultCallback<S, E>{

    fun provideSuccess(data: S)

    fun provideError(error: E)
}