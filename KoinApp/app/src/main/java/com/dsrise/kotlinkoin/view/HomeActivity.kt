package com.dsrise.kotlinkoin.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dsrise.kotlinkoin.R
import com.dsrise.kotlinkoin.cache.MYLRUCache
import com.dsrise.kotlinkoin.databinding.DetailViewBinding
import com.dsrise.kotlinkoin.model.ImageDetail
import com.dsrise.kotlinkoin.utils.Constants
import com.dsrise.kotlinkoin.utils.ImageHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL

class HomeActivity : AppCompatActivity() {

    lateinit var detailViewBinding: DetailViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar()?.hide();
        detailViewBinding = DataBindingUtil.setContentView(this, R.layout.detail_view)
        if (intent != null) {
            val detailString = intent.getStringExtra("DETAIL")
            val gson = Gson()
            val mapType = object : TypeToken<ImageDetail>() {}.type
            val imageDetail: ImageDetail = gson.fromJson(detailString, mapType)
            val res = imageDetail.let {
                val text = "CREATED AT : ${it.created_at}" +
                        "\n PRICE : ${it.price}" +
                        "\n IMAGE_ID : ${it.image_ids}" +
                        "\n NAME : ${it.name}" +
                        "\n UID : ${it.uid}"
                text

            }
            detailViewBinding.detailTv.text = res
            showOrDownloadImage(detailViewBinding.img, imageDetail.image_urls[0])
        }
    }

    private fun showOrDownloadImage(ivImage: ImageView, url: String) {
        if (MYLRUCache.getBitmapFromCache(url) != null) {
            setPbVisibility(false)
            ivImage.setImageBitmap(MYLRUCache.getBitmapFromCache(url))
        } else {
            setPbVisibility(true)
            HttpImageReqTask(object : ImageHelper {
                override fun onImageDownload(result: Bitmap?) {

                    MYLRUCache.addBitmapToCache(url, result!!)
                    ivImage.setImageBitmap(result)
                    setPbVisibility(false)
                }
            }).execute(url)
        }
    }

    internal class HttpImageReqTask(imageHelper: ImageHelper) :
        AsyncTask<String?, Void?, Bitmap?>() {
        var imageHelper: ImageHelper


        init {
            this.imageHelper = imageHelper
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            val imageUrl = params[0]
            try {
                val url = URL(imageUrl)
                Log.d(
                    Constants.APP_LOGGER_TAG,
                    "doInBackground: Image URL Downloading : \n $imageUrl"
                )
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                //                connection.setRequestProperty("Connection", "close");
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                Log.d("TAG", e.message + "")
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            imageHelper.onImageDownload(result)
        }

    }

    fun setPbVisibility(b: Boolean) {
        if (b) {
            detailViewBinding.pb.visibility = View.VISIBLE
            detailViewBinding.img.visibility = View.GONE
        } else {
            detailViewBinding.pb.visibility = View.GONE
            detailViewBinding.img.visibility = View.VISIBLE
        }
    }
}


