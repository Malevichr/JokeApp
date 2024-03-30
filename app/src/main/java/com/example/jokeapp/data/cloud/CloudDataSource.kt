package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.DataSource
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.presentation.ManageResources
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource : DataSource {
    override suspend fun fetch(): JokeResult
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

        override suspend fun fetch(): JokeResult =
            try {
                val response = jokeService.joke().execute()
                JokeResult.Success(response.body()!!, false)
            } catch (e: Exception) {
                JokeResult.Failure(
                    if (e is UnknownHostException || e is ConnectException)
                        noConnection
                    else
                        serviceError
                )
            }

    }
}
