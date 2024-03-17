package com.example.jokeapp

import android.util.Log
import com.google.gson.Gson
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

interface JokeService {
    fun joke(callback: ServiceCallback)
    class Base(
        private val gson: Gson
    ) : JokeService{
        override fun joke(callback: ServiceCallback) {
            Thread{
                var connection: HttpURLConnection? = null
                try{
                    val url = URL("https://api.chucknorris.io/jokes/random")
                    Log.d("Mrp", "start open")
                    connection = url.openConnection() as HttpURLConnection
                    Log.d("Mrp", "connected")
                    val data = connection.inputStream.bufferedReader().readText()
                    val joke = gson.fromJson(data, JokeCloud::class.java)
                    callback.returnSuccess(joke.toJoke())

                }catch (e:Exception){
                    if (e is UnknownHostException || e is ConnectException)
                        callback.returnError(ErrorType.NO_CONNECTION)
                    else
                        callback.returnError(ErrorType.OTHER)
                }finally {
                    connection?.disconnect()
                }
            }.start()
        }
        companion object{
            private const val JOKE_URL = "https://official-joke-api.appspot.com/random_joke"
        }
    }

}

interface ServiceCallback{
    fun returnSuccess(data: String)
    fun returnError(errorType: ErrorType)
}
enum class ErrorType{
    NO_CONNECTION,
    OTHER
}