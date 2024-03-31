package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.presentation.ManageResources

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val manageResources: ManageResources
) : Repository<JokeResult, Error> {
    private var jokeTemporary: Joke? = null

    private var getJokeFromCache = false
    override fun chooseFavorites(favorite: Boolean) {
        getJokeFromCache = favorite
    }
    override suspend fun fetch(): JokeResult {
        val dataSource: DataSource = if(getJokeFromCache)
                cacheDataSource
            else
                cloudDataSource
        val jokeResult = dataSource.fetch()

        jokeTemporary = if(jokeResult.isSuccessful())
                jokeResult
            else
                null

        return jokeResult
    }

    override suspend fun changeJokeStatus(): JokeResult {
        jokeTemporary?.let {
            return it.map(Change(cacheDataSource))
        }
        return JokeResult.Failure(Error.NoFavoriteJoke(manageResources))
    }
}
