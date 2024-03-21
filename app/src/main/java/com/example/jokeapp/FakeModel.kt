package com.example.jokeapp

import java.util.Timer
import java.util.TimerTask

class FakeModel(
    private val manageResources: ManageResources
):Model<Joke, Error> {
    private val noConnection = Error.NoConnection(manageResources)
    private val serviseUnavailable = Error.ServiceUnavailable(manageResources)
    private var resultCallback: ResultCallback<Joke, Error> ? = null
    private var count = 0
    override fun fetch() {
        Timer().schedule(object: TimerTask(){
            override fun run() {
                if (count % 3 == 1){
                    resultCallback?.provideSuccess(Joke("fake joke $count"))
                }
                else if (count % 3 == 2)
                    resultCallback?.provideError(noConnection)
                else
                    resultCallback?.provideError(serviseUnavailable)
                count++
            }
        }, 2000)

    }

    override fun init(resulCallback: ResultCallback<Joke, Error>) {
        this.resultCallback = resulCallback
    }

    override fun clear() {
        resultCallback = null
    }


}