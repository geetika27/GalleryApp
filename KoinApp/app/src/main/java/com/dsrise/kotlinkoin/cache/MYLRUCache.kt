package com.dsrise.kotlinkoin.cache

import android.graphics.Bitmap
import android.util.LruCache

object MYLRUCache {
    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8

    private val memCache: LruCache<String?, Bitmap> =
        object : LruCache<String?, Bitmap>(cacheSize) {
            fun sizeOf(key: Bitmap?, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }

     fun addBitmapToCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromCache(key) == null) {
            memCache.put(key, bitmap)
        }
    }

     fun getBitmapFromCache(key: String): Bitmap? {
        return memCache.get(key)
    }
}