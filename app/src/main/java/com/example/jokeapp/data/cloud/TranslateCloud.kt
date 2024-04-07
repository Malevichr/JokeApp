package com.example.jokeapp.data.cloud

import android.util.Log
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.Language
import com.example.jokeapp.data.LanguageFactory

data class TranslateRequest(
    private val targetLanguageCode:String,
    private val texts:List<String>,
    private val folderId: String = "b1gbvm627trg77pd1eqv"
)

data class TranslateResponse(
    val translations: List<Translation>
){
    data class Translation(
        val text: String,
        val detectedLanguageCode: String
    )
}

class JokeTranslate(
    private val id: String,
    private val text: String,
    private val language: Language
): Joke {
    init {
        Log.d("Mch", "ru2")
    }
    private var translatedText: String = text
    private var translatedTextLanguae: Language = language
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(
        id,
        translatedText,
        translatedTextLanguae
    )
    fun toTranslateRequest(language:Language) = TranslateRequest(language.code(), listOf(text))
    fun implementTranslation(text: String, language: Language) {
        translatedText = text
        translatedTextLanguae = language
    }
}
