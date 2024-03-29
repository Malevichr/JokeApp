package com.example.jokeapp.data

import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
//
//class FakeRepository(
//    private val manageResources: ManageResources
//): Repository<JokeUi, Error> {
//    private val serviceError: Error by lazy {
//        Error.ServiceUnavailable(manageResources)
//    }
//    private var callback: ResultCallback<JokeUi, Error>? = null
//
//    private var count = 0
//    override fun fetch(): JokeResult {
////        when(count++ % 3){
////            0 -> callback?.provideSuccess(JokeUi.Base("Success $count"))
////            1 -> callback?.provideSuccess(JokeUi.Favorite("Favodite $count"))
////            2 -> callback?.provideError(serviceError)
////        }
//        TODO("Not yet implemented")
//    }
//
//    override fun changeJokeStatus(): JokeResult {
//        TODO("Not yet implemented")
//    }
//
//    override fun chooseFavorites(favorite: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//
//}