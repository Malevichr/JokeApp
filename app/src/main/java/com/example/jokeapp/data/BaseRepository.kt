package com.example.jokeapp.data

import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.JokeCloud
import com.example.jokeapp.data.cloud.JokeCloudCallback
import com.example.jokeapp.data.cloud.cache.CacheDataSource
import com.example.jokeapp.data.cloud.cache.JokeCacheCallback
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val manageResources: ManageResources
) : Repository<JokeUi, Error> {
    private var callback: ResultCallback<JokeUi, Error>? = null
    private var jokeCloudTemprorary: JokeCloud? = null
    override fun fetch() {
        if (getJokeFromCache){
            cacheDataSource.fetch(object : JokeCacheCallback{
                override fun provideJoke(joke: JokeCloud) {
                    callback?.provideSuccess(joke.toUiFavorite())
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })
        }else {
            cloudDataSource.fetch(object : JokeCloudCallback {
                override fun provideJokeSuccess(jokeCloud: JokeCloud) {
                    jokeCloudTemprorary = jokeCloud
                    callback?.provideSuccess(jokeCloud.toUi())
                }

                override fun provideError(error: Error) {
                    jokeCloudTemprorary = null
                    callback?.provideError(error)
                }

            })
        }
    }


    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>) {
        jokeCloudTemprorary?.let {
            resultCallback.provideSuccess(it.change(cacheDataSource))
        }
    }
    private var getJokeFromCache = false
    override fun chooseFavorites(favorite: Boolean) {
        getJokeFromCache = favorite
    }

    override fun init(resultCallback: ResultCallback<JokeUi, Error>) {
        callback = resultCallback
    }
}