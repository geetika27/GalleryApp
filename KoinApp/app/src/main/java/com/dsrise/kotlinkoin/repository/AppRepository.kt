package com.dsrise.kotlinkoin.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dsrise.kotlinkoin.di.apiinterface.MyAPIInterface
import com.dsrise.kotlinkoin.model.GalleryResponse

class AppRepository constructor(
    private val fakerAPI: MyAPIInterface, private val context: Context
) {
    private val mImages = MutableLiveData<GalleryResponse>()
    val imageList: LiveData<GalleryResponse>
        get() = mImages

    suspend fun getProducts() {
        val result = fakerAPI.getAllImageDetail()
        if (result.isSuccessful && result.body() != null) {
            mImages.postValue(result.body())
        } else {
            if (result.errorBody() != null) {
                Toast.makeText(context, "ERROR : ${result.message()}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "ERROR : SOMETHING WENT WRONG", Toast.LENGTH_LONG).show()

            }
        }
    }
}