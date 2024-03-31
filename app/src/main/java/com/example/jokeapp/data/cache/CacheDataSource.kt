package com.example.jokeapp.data.cache

import com.example.jokeapp.data.DataSource
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.ToCache
import com.example.jokeapp.presentation.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

interface CacheDataSource : DataSource {
    suspend fun addOrRemove(id: String, joke: Joke): JokeResult
    override suspend fun fetch(): JokeResult
    class Base(
        private val realmConfiguration: RealmConfiguration,
        manageResources: ManageResources
    ) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }

        override suspend fun addOrRemove(id: String, joke: Joke): JokeResult {
            val realm = Realm.open(realmConfiguration)
            return try {
                val jokeCache = realm.query<JokeCache>("id = $0", id).find().first()

                realm.writeBlocking {
                    findLatest(jokeCache)?.also { delete(it) }
                }

                realm.close()
                JokeResult.Success(joke, false)
            } catch (_: Exception) {
                val cacheJoke = joke.map(ToCache())

                realm.writeBlocking {
                    copyToRealm(cacheJoke)
                }

                realm.close()
                JokeResult.Success(joke, true)
            }
        }

        override suspend fun fetch(): JokeResult {
            val realm = Realm.open(realmConfiguration)
            return try {
                val jokesCached = realm.query<JokeCache>().find()
                val randomeJokeCached = jokesCached.random()
                JokeResult.Success(randomeJokeCached, true)
            } catch (_: Exception) {
                JokeResult.Failure(error)
            }
        }
    }


//    class Fake(manageResources: ManageResources) : CacheDataSource {
//        private val error: Error by lazy {
//            Error.NoFavoriteJoke(manageResources)
//        }
//        private val map = mutableMapOf<String, Joke>()
//        override fun addOrRemove(id: String, joke: Joke): JokeResult {
////            return if (map.containsKey(id)) {
////                map.remove(id)
////                joke.map(ToBaseUi())
////            } else {
////                map[id] = joke
////                joke.map(ToFavoriteUi())
////            }
//            TODO("not implemented")
//        }
//
//        override suspend fun fetch(): JokeResult {
//            return if (map.isEmpty()) {
//                JokeResult.Failure(error)
//            } else
//                JokeResult.Success(map.toList()[(0 until (map.size)).random()].second, true)
//        }
//    }
}




