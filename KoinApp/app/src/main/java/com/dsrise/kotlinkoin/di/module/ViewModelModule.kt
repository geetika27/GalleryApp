package com.dsrise.kotlinkoin.di.module

import com.dsrise.kotlinkoin.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ProductViewModel(get())
    }
}