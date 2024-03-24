package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.R


abstract class JokeUi(
    private val text: String,
    @DrawableRes
    private val iconResId: Int
) {
    fun show(jokeUiCallback: JokeUiCallback){
        jokeUiCallback.provideText(text)
        jokeUiCallback.provideIconResId(iconResId)
    }
    class Base(text: String): JokeUi(text, R.drawable.ic_favorite_empty_48)
    class Favorite(text: String): JokeUi(text, R.drawable.ic_favorite_filled_48)
    class Failed(text: String): JokeUi(text, 0)
}
