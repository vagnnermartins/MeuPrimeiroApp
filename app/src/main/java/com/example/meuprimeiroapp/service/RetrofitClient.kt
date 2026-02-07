package com.example.meuprimeiroapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A singleton object that provides a Retrofit instance and an implementation of the [ItemApiService].
 */
object RetrofitClient {

    /**
     * The base URL for the API.
     * The address `http://10.0.2.2:3000/` is used to access the localhost from the Android emulator.
     */
    private const val BASE_URL = "http://10.0.2.2:3000/" // Endereço usado para acessar o localhost no emulador Android

    /**
     * The Retrofit instance, created lazily.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * The implementation of the [ItemApiService], created lazily.
     */
    val apiService: ItemApiService by lazy {
        retrofit.create(ItemApiService::class.java)
    }
}