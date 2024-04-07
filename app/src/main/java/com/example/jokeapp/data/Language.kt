package com.example.jokeapp.data

import com.example.jokeapp.R
import com.example.jokeapp.presentation.ManageResources

interface Language {
    fun code(): String

    class English : Language {
        override fun code() = "en"
    }

    class Russian : Language {
        override fun code() = "ru"
    }

    class Chuvash : Language {
        override fun code() = "cv"
    }
}

class LanguageFactory(
    private val manageResources: ManageResources
) {
    fun createByCode(languageCode: String): Language =
        when (languageCode) {
            "en" -> Language.English()
            "ru" -> Language.Russian()
            "cv" -> Language.Chuvash()
            else -> Language.English()
        }

    fun createByStringName(language: String): Language =
        when(language){
            manageResources.string(R.string.english)-> Language.English()
            manageResources.string(R.string.russian) -> Language.Russian()
            manageResources.string(R.string.chuvash) -> Language.Chuvash()
            else -> Language.English()
        }
}
