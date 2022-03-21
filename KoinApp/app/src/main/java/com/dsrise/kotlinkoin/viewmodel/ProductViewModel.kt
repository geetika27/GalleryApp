package com.dsrise.kotlinkoin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsrise.kotlinkoin.model.GalleryResponse
import com.dsrise.kotlinkoin.repository.AppRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: AppRepository
) : ViewModel() {
    val mDetailList: LiveData<GalleryResponse>
        get() = repository.imageList

    init {
        viewModelScope.launch {
            repository.getProducts()
        }

    }


}