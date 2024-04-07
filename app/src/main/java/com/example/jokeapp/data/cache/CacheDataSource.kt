package com.example.jokeapp.data.cache

import com.example.jokeapp.data.DataSource
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Language
import com.example.jokeapp.data.ToCache
import com.example.jokeapp.presentation.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

interface CacheDataSource : DataSource {
    suspend fun addOrRemove(id: String, joke: Joke, language: Language): JokeResult
    override suspend fun fetch(language: Language): JokeResult
    class Base(
        private val realmConfiguration: RealmConfiguration,
        manageResources: ManageResources
    ) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }

        override suspend fun addOrRemove(id: String, joke: Joke, language: Language): JokeResult {
            val realm = Realm.open(realmConfiguration)
            return try {
                val jokeCache = realm.query<JokeCache>("id = $0", id).find().first()

                realm.writeBlocking {
                    findLatest(jokeCache)?.also { delete(it) }
                }

                realm.close()
                JokeResult.Success(joke, false, language)
            } catch (_: Exception) {
                val cacheJoke = joke.map(ToCache())

                realm.writeBlocking {
                    copyToRealm(cacheJoke)
                }

                realm.close()
                JokeResult.Success(joke, true, language)
            }
        }

        override suspend fun fetch(language: Language): JokeResult {
            val realm = Realm.open(realmConfiguration)
            return try {
                val jokesCached = realm.query<JokeCache>().find()
                val randomeJokeCached = jokesCached.random()
                JokeResult.Success(randomeJokeCached, true, language)
            } catch (_: Exception) {
                JokeResult.Failure(error)
            }
        }
    }


}




