package com.lfelipe.whatsapp.di

import com.lfelipe.whatsapp.repository.CountriesRepository
import com.lfelipe.whatsapp.viewmodel.CountriesViewModel
import com.lfelipe.whatsapp.viewmodel.PhoneRegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val module = module {

    single{
        CountriesRepository()
    }

    viewModel {
        CountriesViewModel(
            get()
        )
    }

    viewModel {
        PhoneRegisterViewModel(
            get()
        )
    }
}