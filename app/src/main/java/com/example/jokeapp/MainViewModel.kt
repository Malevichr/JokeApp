package com.example.jokeapp

import android.provider.ContactsContract.Data

class MainViewModel(private val model: Model<Any, Any>) {
    private var textCallback: TextCallback = TextCallback.Empty()
    fun getJoke() {
        model.fetch()
    }

    fun init(textCallback: TextCallback) {
        this.textCallback = textCallback
        model.init(object: ResultCallback<Any, Any>{
            override fun provideSuccess(data: Any) {
                textCallback.provideText("success")
            }

            override fun provideError(error: Any) {
                textCallback.provideText("error")
            }
        })
    }

    fun clear() {
        textCallback = TextCallback.Empty()
        model.clear()
    }

}

interface TextCallback{

    fun provideText(text: String)

    class Empty: TextCallback{
        override fun provideText(text: String) = Unit
    }
}