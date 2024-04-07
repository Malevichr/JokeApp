package com.example.jokeapp.data.cloud

import android.util.Log
import com.example.jokeapp.data.DataSource
import com.example.jokeapp.data.Error
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Language
import com.example.jokeapp.data.LanguageFactory
import com.example.jokeapp.data.ToTranslate
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource : DataSource {
    override suspend fun fetch(language: Language): JokeResult
    class Base(
        private val jokeService: JokeService,
        private val translateService: TranslateService,
        private val manageResources: ManageResources,
        private val toTranslate: Joke.Mapper<JokeTranslate> = ToTranslate(),
        private val handleRequest: HandleRequest = HandleRequest.Base(manageResources),
        private val languageFactory: LanguageFactory = LanguageFactory(manageResources)
    ) : CloudDataSource {

        override suspend fun fetch(language: Language): JokeResult =
            handleRequest.handle {
                val response = jokeService.joke().execute()
                Log.d("Mch", "${response.code()} <<joke>> ${language.code()}")
                if(language is Language.English)
                    JokeResult.Success(response.body()!!, false, language)
                else {

                    fetchJokeTranslate(response.body()!!, language)
                }
            }
        suspend fun fetchJokeTranslate(joke: Joke, language: Language): JokeResult =
            handleRequest.handle {
                Log.d("Mch", "${language.code()}")
                val jokeTranslate = joke.map(toTranslate)
                Log.d("Mch", "")
                val translateRequest = jokeTranslate.toTranslateRequest(language)
                Log.d("Mch", "ru2")
                val translateResponse = translateService.translate(translateRequest).execute()
                Log.d("Mch", "${translateResponse.code()} <<translate>> ${translateResponse.message()}")
                val translation = translateResponse.body()!!.translations[0]


                jokeTranslate.implementTranslation(
                    translation.text,
                    languageFactory.createByCode(translation.detectedLanguageCode)
                    )

                JokeResult.Success(jokeTranslate, false, language)
            }
    }
}
interface HandleRequest{
    suspend fun handle(request: suspend ()-> JokeResult): JokeResult
    class Base(
        private val manageResources: ManageResources
    ): HandleRequest{
        private val noConnection: Error by lazy {
            Error.NoConnection(manageResources)
        }
        private val serviceError: Error by lazy {
            Error.ServiceUnavailable(manageResources)
        }
        override suspend fun handle(request: suspend () -> JokeResult) =
            try{
                request()
            }catch (e: Exception) {
                Log.d("Mch", "${e.message}")
                JokeResult.Failure(
                    if (e is UnknownHostException || e is ConnectException)
                        noConnection
                    else
                        serviceError
                )
            }
    }
}


