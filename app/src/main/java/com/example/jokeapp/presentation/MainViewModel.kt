package com.example.jokeapp.presentation

import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi


class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: ToFavoriteUi = ToFavoriteUi(),
    private val toBaseUi: ToBaseUi = ToBaseUi()
) {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()

    fun getJoke() {
        Thread {
            val result = repository.fetch()
            if (result.isSuccessful()) {
                if (result.isFavorite())
                    result.map(toFavorite).show(jokeUiCallback)
                else
                    result.map(toBaseUi).show(jokeUiCallback)
            } else {
                JokeUi.Failed(result.errorMessage()).show(jokeUiCallback)
            }
        }.start()
    }

    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    fun clear() {
        jokeUiCallback = JokeUiCallback.Empty()
    }

    fun chooseFavorites(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeJokeStatus() {
        Thread{
            val result = repository.changeJokeStatus()
            if(result.isFavorite())
                result.map(toFavorite).show(jokeUiCallback)
            else
                result.map(toBaseUi).show(jokeUiCallback)
        }.start()
    }
}


interface JokeUiCallback {
    fun provideText(text: String)
    fun provideIconResId(iconResId: Int)

    class Empty : JokeUiCallback {
        override fun provideText(text: String) = Unit
        override fun provideIconResId(iconResId: Int) = Unit
    }
}