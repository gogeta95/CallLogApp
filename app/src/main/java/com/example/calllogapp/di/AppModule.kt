package com.example.calllogapp.di

import com.example.calllogapp.interactor.CallLogInteractor
import com.example.calllogapp.interactor.CallLogInteractorImpl
import com.example.calllogapp.interactor.CurrentCallQueryWatcher
import com.example.calllogapp.interactor.CurrentCallQueryWatcherImpl
import com.example.calllogapp.repository.*
import com.example.calllogapp.server.CallLogServer
import com.example.calllogapp.server.CallLogServerImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single<DataRepository> { DataRepositoryImpl(get()) }

    single<ServerInfoProvider> { ServerInfoProviderImpl(androidApplication()) }

    single<ContactsRepository> { ContactsRepositoryImpl(androidApplication()) }

    single<CallLogInteractor> { CallLogInteractorImpl(get(), get(), Dispatchers.IO, get()) }

    factory<CallLogServer> { CallLogServerImpl(Dispatchers.IO, get(), get()) }

    single<CurrentCallQueryWatcher> { CurrentCallQueryWatcherImpl() }

}