package com.dsrise.kotlinkoin.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsrise.kotlinkoin.model.GalleryResponse
import com.dsrise.kotlinkoin.repository.AppRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: AppRepository, private val context: Context
) : ViewModel() {
    val mDetailList: LiveData<GalleryResponse>
        get() = repository.imageList
    private val myCoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Toast.makeText(context, "ERROR {${throwable.message}}", Toast.LENGTH_LONG).show()
        }

    init {
        viewModelScope.launch(myCoroutineExceptionHandler) {
            repository.getProducts()
        }

    }


}