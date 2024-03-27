package com.example.jokeapp.data.cloud.cache

import com.example.jokeapp.data.Error
import com.example.jokeapp.data.cloud.JokeCloud
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

interface CacheDataSource {
    fun addOrRemove(id: String, joke: JokeCloud): JokeUi
    fun fetch(jokeCacheCallback: JokeCacheCallback)
    class Base(
        private val realmConfiguration: RealmConfiguration,
        manageResources: ManageResources
    ) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }

        override fun addOrRemove(id: String, joke: JokeCloud): JokeUi {
            val realm = Realm.open(realmConfiguration)


            return try {
                val jokeCache = realm.query<JokeCache>("id = $0", id).find().first()
                realm.writeBlocking {
                    findLatest(jokeCache)?.also { delete(it) }
                }
                realm.close()
                joke.toUi()
            } catch (_: Exception) {
                realm.writeBlocking {
                    copyToRealm(joke.toCache())
                }
                realm.close()
                joke.toUiFavorite()
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            val realm = Realm.open(realmConfiguration)
            try {
                val jokesCached = realm.query<JokeCache>().find()
                val r = jokesCached.random()
                jokeCacheCallback.provideJoke(JokeCloud(r.id, r.text))
            } catch (_: Exception) {
                jokeCacheCallback.provideError(error)
            }
        }

    }

    class Fake(manageResources: ManageResources) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }
        private val map = mutableMapOf<String, JokeCloud>()
        override fun addOrRemove(id: String, joke: JokeCloud): JokeUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                joke.toUi()
            } else {
                map[id] = joke
                joke.toUiFavorite()
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            if (map.isEmpty()) {
                jokeCacheCallback.provideError(error)
            } else
                jokeCacheCallback.provideJoke(map.toList()[(0 until (map.size)).random()].second)
        }
    }
}

interface JokeCacheCallback : ProvideError {
    fun provideJoke(joke: JokeCloud)

}

interface ProvideError {
    fun provideError(error: Error)
}