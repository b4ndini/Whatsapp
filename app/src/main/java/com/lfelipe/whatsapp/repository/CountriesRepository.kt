package com.lfelipe.whatsapp.repository

import com.lfelipe.whatsapp.api.ApiService
import com.lfelipe.whatsapp.model.Country
import io.reactivex.Observable

class CountriesRepository {

    fun getCountryByCode(code: String): Observable<Country> {

        return ApiService.api.getCountry(code)

    }

    fun getCountries(): Observable<Country> {

        return ApiService.api.getAllCountries()

    }

}