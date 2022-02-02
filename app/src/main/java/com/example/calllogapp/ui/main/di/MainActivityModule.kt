package com.example.calllogapp.ui.main.di

import com.example.calllogapp.ui.main.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule  = module {
    viewModel { MainActivityViewModel(get(), get()) }
}