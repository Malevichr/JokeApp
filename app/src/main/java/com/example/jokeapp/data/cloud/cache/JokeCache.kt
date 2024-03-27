package com.example.jokeapp.data.cloud.cache

import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeCache(): RealmObject {
    @PrimaryKey
    var id: String = ""
    var text: String = ""
}