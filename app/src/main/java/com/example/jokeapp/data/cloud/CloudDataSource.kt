package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.DataSource
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.cache.ProvideError
import com.example.jokeapp.presentation.ManageResources
import retrofit2.Call
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource: DataSource {
    override fun fetch(jokeCallback: JokeCallback)
    class Base(
        private val jokeService: JokeService,
        private val manageResources: ManageResources
    ) : CloudDataSource {
        private val noConnection: Error by lazy {
            Error.NoConnection(manageResources)
        }
        private val serviceError: Error by lazy {
            Error.ServiceUnavailable(manageResources)
        }

        override fun fetch(jokeCallback: JokeCallback) {

            jokeService.joke().enqueue(object : retrofit2.Callback<JokeCloud> {
                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null)
                            jokeCallback.provideJoke(body)
                        else
                            jokeCallback.provideError(serviceError)
                    } else
                        jokeCallback.provideError(serviceError)
                }

                override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                    jokeCallback.provideError(
                        if (t is UnknownHostException || t is ConnectException)
                            noConnection
                        else
                            serviceError
                    )
                }
            })
        }
    }
}

interface JokeCallback: ProvideError {
    fun provideJoke(joke: Joke)
}