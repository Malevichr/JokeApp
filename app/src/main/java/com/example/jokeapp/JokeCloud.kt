package com.example.jokeapp

import com.google.gson.annotations.SerializedName

data class JokeCloud(
    @SerializedName("categories")
    private val category: Array<Any>,
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
    fun toJoke() = value
}
