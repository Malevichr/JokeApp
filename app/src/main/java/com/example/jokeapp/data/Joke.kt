package com.example.jokeapp.data

import android.util.Log
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.data.cloud.JokeTranslate
import com.example.jokeapp.presentation.JokeUi

interface Joke {
    suspend fun <T> map(mapper: Mapper<T>): T
    interface Mapper<T> {
        suspend fun map(id: String, text: String, language: Language): T
    }
}

data class JokeDomain(
    private val id: String,
    private val text: String,
    private val language: Language
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>) = mapper.map(id, text, language)

}

class ToCache : Joke.Mapper<JokeCache> {
    override suspend fun map(id: String, text: String, language: Language): JokeCache {
        val jokeCache = JokeCache()
        jokeCache.id = id
        jokeCache.text = text
        return jokeCache
    }
}

class ToBaseUi : Joke.Mapper<JokeUi> {
    override suspend fun map(id: String, text: String, language: Language): JokeUi = JokeUi.Base(text)

}

class ToFavoriteUi : Joke.Mapper<JokeUi> {
    override suspend fun map(id: String, text: String, language: Language): JokeUi = JokeUi.Favorite(text)
}

class Change(
    private val cacheDataSource: CacheDataSource,
    private val toDomain: Joke.Mapper<JokeDomain> = ToDomain()
) : Joke.Mapper<JokeResult> {
    override suspend fun map(id: String, text: String, language: Language): JokeResult =
        cacheDataSource.addOrRemove(id, toDomain.map(id, text, language), language)
}

class ToDomain : Joke.Mapper<JokeDomain> {
    override suspend fun map(id: String, text: String, language: Language): JokeDomain {
        return JokeDomain(id, text, language)
    }
}
class ToTranslate : Joke.Mapper<JokeTranslate>{
    override suspend fun map(id: String, text: String, language: Language): JokeTranslate{
        Log.d("Mch", "ru1")
        return JokeTranslate(id, text, language)
    }
}