package com.example.jokeapp

import com.example.jokeapp.ErrorType.*

class BaseModel(
    private val jokeService: JokeService,
    private val manageResources: ManageResources
) : Model<Joke, Error> {
    private val noConnection: Error by lazy {
        Error.NoConnection(manageResources)
    }
    private val serviceError: Error by lazy {
        Error.ServiceUnavailable(manageResources)
    }
    private var callback: ResultCallback<Joke, Error>? = null
    override fun fetch() {
        jokeService.joke(object : ServiceCallback {
            override fun returnSuccess(data: String) {
                callback?.provideSuccess(Joke(data, ""))
            }

            override fun returnError(errorType: ErrorType) {
                when (errorType) {
                    NO_CONNECTION -> callback?.provideError(noConnection)
                    OTHER -> callback?.provideError(serviceError)
                }
            }

        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}