package com.lfelipe.whatsapp.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    val api = getRestCountriesApiClient().create(RestCountriesApi::class.java)

    private fun getRestCountriesApiClient(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://restcountries.eu/rest/v2/")
            .build()
    }

}