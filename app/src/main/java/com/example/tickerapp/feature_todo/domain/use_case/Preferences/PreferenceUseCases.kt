package com.example.tickerapp.feature_todo.domain.use_case.Preferences

data class PreferenceUseCases(
    val observeBoolean: ObserveBoolean,
    val observeString: ObserveString,
    val getBoolean: GetBoolean,
    val getString: GetString,
    val putBoolean: PutBoolean,
    val putString: PutString
)