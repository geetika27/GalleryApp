package com.dsrise.kotlinkoin.di.apiinterface

import com.dsrise.kotlinkoin.model.GalleryResponse
import retrofit2.Response
import retrofit2.http.GET

interface MyAPIInterface {

    @GET("default/dynamodb-writer")
    suspend fun getAllImageDetail(): Response<GalleryResponse>
}