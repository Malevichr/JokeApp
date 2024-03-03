package com.example.jokeapp


interface Model<S, E> {
    fun fetch()
    fun init(callback: ResultCallback<Any, Any>)
    fun clear()
}
interface ResultCallback<S, E>{

    fun provideSuccess(data: S)

    fun provideError(error: E)
}