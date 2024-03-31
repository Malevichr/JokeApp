package com.example.jokeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val repository: Repository<JokeResult, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi>  = ToBaseUi(),
    private val dispatcherList: DispatcherList = DispatcherList.Base()
) : ViewModel() {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    fun getJoke() {
        viewModelScope.launch(dispatcherList.io()) {
            val result = repository.fetch()

            val ui: JokeUi = if (result.isSuccessful())
                result.map(if (result.isFavorite()) toFavorite else toBaseUi)
            else
                JokeUi.Failed(result.errorMessage())

            withContext(dispatcherList.ui()) {
                ui.show(jokeUiCallback)
            }
        }
    }

    fun changeJokeStatus() {
        viewModelScope.launch(dispatcherList.io()) {
            val result = repository.changeJokeStatus()

            val job = result.map(if (result.isFavorite()) toFavorite else toBaseUi)

            withContext(dispatcherList.ui()) {
                job.show(jokeUiCallback)
            }
        }
    }

    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    override fun onCleared() {
        super.onCleared()
        jokeUiCallback = JokeUiCallback.Empty()
    }

    fun chooseFavorites(favorites: Boolean) {
        repository.chooseFavorites(favorites)
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

interface DispatcherList {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    class Base:DispatcherList {
        override fun io() = Dispatchers.IO
        override fun ui() = Dispatchers.Main
    }
}