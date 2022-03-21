package com.dsrise.kotlinkoin.model

data class GalleryResponse(
    val pagination: Pagination,
    val results: List<ImageDetail>
)