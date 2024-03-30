package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val manageResources: ManageResources
) : Repository<JokeUi, Error> {
    private var jokeTemrorary: Joke? = null

    override suspend fun fetch(): JokeResult {
        val jokeResult = if (getJokeFromCache)
            cacheDataSource.fetch()
        else
            cloudDataSource.fetch()
        jokeTemrorary = if(jokeResult.isSuccessful()){
            jokeResult
        } else
            null

        return jokeResult
    }

    override suspend fun changeJokeStatus(): JokeResult {
        jokeTemrorary?.let {
            return it.map(Change(cacheDataSource))
        }
        return JokeResult.Failure(Error.NoFavoriteJoke(manageResources))
    }

    private var getJokeFromCache = false
    override fun chooseFavorites(favorite: Boolean) {
        getJokeFromCache = favorite
    }
}
