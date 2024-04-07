package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.Joke
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language


data class JokeCloud(
    @SerializedName("id")
    private val id: String,
    @SerializedName("value")
    private val text: String,

) : Joke {

    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(id, text, com.example.jokeapp.data.Language.English())

}
