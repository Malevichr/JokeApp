package com.example.jokeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: ToFavoriteUi = ToFavoriteUi(),
    private val toBaseUi: ToBaseUi = ToBaseUi()
):ViewModel() {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    fun getJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetch()
            val ui = if (result.isSuccessful()) {
                if (result.isFavorite())
                    result.map(toFavorite)
                else
                    result.map(toBaseUi)
            } else {
                JokeUi.Failed(result.errorMessage())
            }
            withContext(Dispatchers.Main){
                ui.show(jokeUiCallback)
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

    fun changeJokeStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.changeJokeStatus()
            val job = if(result.isFavorite())
                result.map(toFavorite)
            else
                result.map(toBaseUi)

            withContext(Dispatchers.Main){
                job.show(jokeUiCallback)
            }
        }
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