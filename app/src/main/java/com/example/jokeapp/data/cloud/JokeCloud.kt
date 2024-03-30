package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.Joke
import com.google.gson.annotations.SerializedName


data class JokeCloud(
    @SerializedName("categories")
    private val category: Array<String>,
    @SerializedName("created_at")
    private val created_at: String,
    @SerializedName("icon_url")
    private val icon_url: String,
    @SerializedName("id")
    private val id: String,
    @SerializedName("updated_at")
    private val updated_at: String,
    @SerializedName("url")
    private val url: String,
    @SerializedName("value")
    private val text: String
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(id, text)

}
