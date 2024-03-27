package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.cloud.cache.CacheDataSource
import com.example.jokeapp.data.cloud.cache.JokeCache
import com.example.jokeapp.presentation.JokeUi
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
    private val value: String
){
    constructor(id: String, text:String):
            this(arrayOf(""),"","",id, "","",text)
    fun toUi() = JokeUi.Base(value)
    fun toUiFavorite() = JokeUi.Favorite(value)
    fun change(cacheDataSource: CacheDataSource): JokeUi =
        cacheDataSource.addOrRemove(id, this)
    fun toCache(): JokeCache{
        val jokeCache = JokeCache()
        jokeCache.id = id
        jokeCache.text = value
        return jokeCache
    }
}
