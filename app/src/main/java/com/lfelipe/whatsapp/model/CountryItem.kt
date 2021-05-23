package com.lfelipe.whatsapp.model

data class CountryItem(
    val callingCodes: List<String>,
    val capital: String,
    val flag: String,
    val languages: List<Language>,
    val alpha2Code: String,
    val name: String,
    val nativeName: String,
    val translations: Translations
)