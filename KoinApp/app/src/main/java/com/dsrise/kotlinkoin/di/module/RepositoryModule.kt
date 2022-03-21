package com.dsrise.kotlinkoin.di.module

import com.dsrise.kotlinkoin.repository.AppRepository
import org.koin.dsl.module

val repositoryModule  = module{
    single { AppRepository(get(),get()) }
}