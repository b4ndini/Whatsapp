package com.lfelipe.whatsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lfelipe.whatsapp.model.Country
import com.lfelipe.whatsapp.repository.CountriesRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CountriesViewModel(private val repository: CountriesRepository) : ViewModel() {

    private lateinit var disposable: Disposable
    var countryLiveData: MutableLiveData<Country> = MutableLiveData()
    var errorMsgLiveData: MutableLiveData<String> = MutableLiveData()

    fun getCountries(){
        repository.getCountries()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Country> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(data: Country) {
                    countryLiveData.postValue(data)
                }

                override fun onError(e: Throwable) {
                    errorMsgLiveData.postValue("Error + ${e.localizedMessage}")
                }

                override fun onComplete() {
                    //
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        if(!disposable.isDisposed){
            disposable.dispose()
        }
    }

}


