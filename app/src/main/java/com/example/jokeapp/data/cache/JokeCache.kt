package com.example.jokeapp.data.cache

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.Language
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeCache(): RealmObject, Joke {
    @PrimaryKey
    var id: String = ""
    var text: String = ""
    //var language: String = Language.English().code()
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(id, text, Language.English())
}