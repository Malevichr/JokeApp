package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val repository: Repository<JokeResult, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    private val communication: JokeCommunication,
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList), Observe<JokeUi> {
    fun getJoke() = handle(
        {
            val result = repository.fetch()
            if (result.isSuccessful())
                result.map(if (result.isFavorite()) toFavorite else toBaseUi)
            else
                JokeUi.Failed(result.errorMessage())
        }
    ) {
        communication.map(it)
    }

    fun changeJokeStatus() = handle(
        {
            val result = repository.changeJokeStatus()
            result.map(if (result.isFavorite()) toFavorite else toBaseUi)
        }
    ) {
        communication.map(it)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<JokeUi>) {
        communication.observe(owner, observer)
    }

    fun chooseFavorites(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }
}

abstract class BaseViewModel(private val dispatcherList: DispatcherList) : ViewModel() {
    fun <T> handle(
        blockIo: suspend () -> T,
        blockUi: suspend (T) -> Unit
    ) = viewModelScope.launch(dispatcherList.io()) {
        val result = blockIo.invoke()
        withContext(dispatcherList.ui()) {
            blockUi.invoke(result)
        }
    }
}
interface Communication<T: Any>: Observe<T>{
    fun map(data: T)


    abstract class Base<T: Any>(
        private val liveData: MutableLiveData<T> = MutableLiveData()
    ): Communication<T>{
        override fun map(data: T) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }
}

interface Observe<T: Any>{
    fun observe(owner: LifecycleOwner, observer: Observer<T>) = Unit
}

interface JokeCommunication: Communication<JokeUi>{
    class Base : Communication.Base<JokeUi>(), JokeCommunication
}

interface JokeUiCallback {
    fun provideText(text: String)
    fun provideIconResId(iconResId: Int)
}



