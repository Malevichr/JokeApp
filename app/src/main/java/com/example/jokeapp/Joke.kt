package com.example.jokeapp


class Joke(private val text: String, private val punchLine:String) {
    fun toUi() = "$text\n$punchLine"
}