package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.presentation.JokeUi

interface Joke {
    suspend fun <T> map(mapper: Mapper<T>): T
    interface Mapper<T> {
        suspend fun map(id: String, text: String): T
    }
}

data class JokeDomain(
    private val id: String,
    private val text: String
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>) = mapper.map(id, text)

}

class ToCache : Joke.Mapper<JokeCache> {
    override suspend fun map(id: String, text: String): JokeCache {
        val jokeCache = JokeCache()
        jokeCache.id = id
        jokeCache.text = text
        return jokeCache
    }
}

class ToBaseUi : Joke.Mapper<JokeUi> {
    override suspend fun map(id: String, text: String): JokeUi = JokeUi.Base(text)

}

class ToFavoriteUi : Joke.Mapper<JokeUi> {
    override suspend fun map(id: String, text: String): JokeUi = JokeUi.Favorite(text)
}

class Change(
    private val cacheDataSource: CacheDataSource,
    private val toDomain: Joke.Mapper<JokeDomain> = ToDomain()
) : Joke.Mapper<JokeResult> {
    override suspend fun map(id: String, text: String): JokeResult =
        cacheDataSource.addOrRemove(id, toDomain.map(id, text))
}

class ToDomain : Joke.Mapper<JokeDomain> {
    override suspend fun map(id: String, text: String): JokeDomain {
        return JokeDomain(id, text)
    }
}