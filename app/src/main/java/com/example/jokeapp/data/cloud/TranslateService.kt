package com.example.jokeapp.data.cloud

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TranslateService {
    @Headers("Authorization: Bearer t1.9euelZrJl8mLisrHyJKOiYnKlIySxu3rnpWakMzJlpaWk82JmZubk5mbmZfl8_dIJDVP-e9AYx1o_t3z9whTMk_570BjHWj-zef1656Vmo6Wx5DOyZidyo-clMrLjs-c7_zF656Vmo6Wx5DOyZidyo-clMrLjs-c.ieSMIyOw01RHSyvxLMfYTArBFt-u_uNuJiTNlGjjt8Yzftb5GCX5QTEZxKoKbjXdBHV-DqrDJtfYI2EAE10uDA")
    @POST("/translate/v2/translate")
    fun translate(@Body body: TranslateRequest): Call<TranslateResponse>
}