package com.example.jokeapp.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherList {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    class Base : DispatcherList {
        override fun io() = Dispatchers.IO
        override fun ui() = Dispatchers.Main
    }
}