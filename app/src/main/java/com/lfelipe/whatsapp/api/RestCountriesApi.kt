package com.lfelipe.whatsapp.api

import com.lfelipe.whatsapp.model.Country
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesApi {

    @GET("callingcode/{callingcode}")
    fun getCountry(
        @Path("callingcode") code: String
    ): Observable<Country>

    @GET("all")
    fun getAllCountries() : Observable<Country>

}