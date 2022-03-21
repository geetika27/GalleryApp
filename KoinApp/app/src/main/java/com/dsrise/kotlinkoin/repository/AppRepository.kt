package com.dsrise.kotlinkoin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dsrise.kotlinkoin.di.apiinterface.MyAPIInterface
import com.dsrise.kotlinkoin.model.GalleryResponse

class AppRepository constructor(
    private val fakerAPI: MyAPIInterface
) {
    private val mImages = MutableLiveData<GalleryResponse>()
    val imageList: LiveData<GalleryResponse>
        get() = mImages

    suspend fun getProducts() {
        val result = fakerAPI.getAllImageDetail()
        if (result.isSuccessful && result.body() != null) {
            mImages.postValue(result.body())
        }
    }
}