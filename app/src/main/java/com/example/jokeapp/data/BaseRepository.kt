package com.example.jokeapp.data

import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.JokeCallback
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.presentation.JokeUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) : Repository<JokeUi, Error> {
    private var callback: ResultCallback<JokeUi, Error>? = null
    private var jokeTemrorary: Joke? = null
    override fun fetch() {
        if (getJokeFromCache){
            cacheDataSource.fetch(object : JokeCallback {
                override fun provideJoke(joke: Joke) {
                    jokeTemrorary = joke
                    callback?.provideSuccess(joke.map(ToFavoriteUi()))
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })
        }else {
            cloudDataSource.fetch(object :
                com.example.jokeapp.data.cloud.JokeCallback {
                override fun provideJoke(joke: Joke) {
                    jokeTemrorary = joke
                    callback?.provideSuccess(joke.map(ToBaseUi()))
                }

                override fun provideError(error: Error) {
                    jokeTemrorary = null
                    callback?.provideError(error)
                }

            })
        }
    }


    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>) {
        jokeTemrorary?.let {
            resultCallback.provideSuccess(it.map(Change(cacheDataSource)))
        }
    }
    private var getJokeFromCache = false
    override fun chooseFavorites(favorite: Boolean) {
        getJokeFromCache = favorite
    }

    override fun init(callback: ResultCallback<JokeUi, Error>) {
        this.callback = callback
    }
}