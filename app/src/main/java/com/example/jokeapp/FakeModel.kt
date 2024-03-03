package com.example.jokeapp

import java.util.Timer
import java.util.TimerTask

class FakeModel:Model<Any, Any> {
    private var resultCallback: ResultCallback<Any, Any> ? = null
    private var count = 0
    override fun fetch() {
        Timer().schedule(object: TimerTask(){
            override fun run() {
                if (++count % 2 == 1){
                    resultCallback?.provideSuccess("")
                }
                else
                    resultCallback?.provideError("")
            }
        }, 5000)

    }

    override fun init(resulCallback: ResultCallback<Any, Any>) {
        this.resultCallback = resulCallback
    }

    override fun clear() {
        resultCallback = null
    }


}