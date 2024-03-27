package com.example.jokeapp.data.cache

import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToCache
import com.example.jokeapp.data.ToFavoriteUi
import com.example.jokeapp.data.cloud.JokeCallback
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

interface CacheDataSource {
    fun addOrRemove(id: String, joke: Joke): JokeUi
    fun fetch(jokeCallback: JokeCallback)
    class Base(
        private val realmConfiguration: RealmConfiguration,
        manageResources: ManageResources
    ) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }

        override fun addOrRemove(id: String, joke: Joke): JokeUi {
            val realm = Realm.open(realmConfiguration)


            return try {
                val jokeCache = realm.query<JokeCache>("id = $0", id).find().first()
                realm.writeBlocking {
                    findLatest(jokeCache)?.also { delete(it) }
                }
                realm.close()
                joke.map(ToBaseUi())
            } catch (_: Exception) {
                realm.writeBlocking {
                    copyToRealm(joke.map(ToCache()))
                }
                realm.close()
                joke.map(ToFavoriteUi())
            }
        }

        override fun fetch(jokeCallback: JokeCallback) {
            val realm = Realm.open(realmConfiguration)
            try {
                val jokesCached = realm.query<JokeCache>().find()
                val randomeJokeCached = jokesCached.random()
                jokeCallback.provideJoke(randomeJokeCached)
            } catch (_: Exception) {
                jokeCallback.provideError(error)
            }
        }

    }

    class Fake(manageResources: ManageResources) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }
        private val map = mutableMapOf<String, Joke>()
        override fun addOrRemove(id: String, joke: Joke): JokeUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                joke.map(ToBaseUi())
            } else {
                map[id] = joke
                joke.map(ToFavoriteUi())
            }
        }

        override fun fetch(jokeCallback: JokeCallback) {
            if (map.isEmpty()) {
                jokeCallback.provideError(error)
            } else
                jokeCallback.provideJoke(map.toList()[(0 until (map.size)).random()].second)
        }
    }
}



interface ProvideError {
    fun provideError(error: Error)
}