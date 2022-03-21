package com.dsrise.kotlinkoin.di.module

import com.dsrise.kotlinkoin.di.apiinterface.MyAPIInterface
import com.dsrise.kotlinkoin.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideFakeAPI() }

}

private val httpInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder().addInterceptor(httpInterceptor).build()

private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

fun provideFakeAPI(): MyAPIInterface {
    return retrofit.create(MyAPIInterface::class.java)
}