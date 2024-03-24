package com.example.jokeapp.data.cloud.cache

import com.example.jokeapp.data.Error
import com.example.jokeapp.data.cloud.JokeCloud
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import kotlin.random.Random

interface CacheDataSource {
    fun addOrRemove(id: String, joke: JokeCloud): JokeUi
    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Fake(manageResources: ManageResources) : CacheDataSource {
        private val error: Error by lazy {
            Error.NoFavoriteJoke(manageResources)
        }
        private val map = mutableMapOf<String, JokeCloud>()
        override fun addOrRemove(id: String, joke: JokeCloud): JokeUi {
            if (map.containsKey(id)) {
                map.remove(id)
                return joke.toUi()
            }
            else{
                map[id] = joke
                return joke.toUiFavorite()
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            if(map.isEmpty()){
                jokeCacheCallback.provideError(error)
            }
            else
                jokeCacheCallback.provideJoke(map.toList()[(0 until(map.size)).random()].second)
            //jokeCacheCallback.provideJoke(JokeCloud(Array<Int>(0){it},"","","","","","cache fake"))
        }
    }
}
interface JokeCacheCallback: ProvideError{
    fun provideJoke(joke: JokeCloud)

}
interface ProvideError{
    fun provideError(error: Error)
}