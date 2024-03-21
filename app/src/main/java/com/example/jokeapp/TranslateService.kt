package com.example.jokeapp

interface TranslateService {
    fun translate(language: String): String
    class Base(
        private val manageResources: ManageResources
    ) : TranslateService {
        override fun translate(language: String): String {
            TODO("Not yet implemented")
        }
    }
}