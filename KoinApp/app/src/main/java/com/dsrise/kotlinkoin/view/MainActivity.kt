package com.dsrise.kotlinkoin.view

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dsrise.kotlinkoin.R
import com.dsrise.kotlinkoin.adapter.ImageViewAdapter
import com.dsrise.kotlinkoin.databinding.ActivityMainBinding
import com.dsrise.kotlinkoin.di.AppComponent
import com.dsrise.kotlinkoin.model.ImageDetail
import com.dsrise.kotlinkoin.utils.Constants
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    val appComponent = AppComponent()
    private lateinit var activityMainBinding: ActivityMainBinding
    private var imageViewAdapter: ImageViewAdapter? = null
    private var mList: List<ImageDetail> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar()?.hide();
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setRecyclerView()
        setPbVisibility(true)
        appComponent.productViewModel.mDetailList.observe(this, Observer {
            setPbVisibility(false)
            val list = it.results
            imageViewAdapter?.updateItems(list)
        })

    }

    private fun setRecyclerView() {
        imageViewAdapter = ImageViewAdapter(mList, object : ImageViewAdapter.ItemClickListener {
            override fun onItemClick(position: Int, imageDetail: ImageDetail) {
                Log.d(Constants.APP_LOGGER_TAG, "onItemClick: ImageDetail : $imageDetail")
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                val imageString = Gson().toJson(imageDetail, ImageDetail::class.java)
                intent.putExtra("DETAIL", imageString)
                startActivity(intent)
            }
        })
        activityMainBinding.rvGallery.adapter = imageViewAdapter
    }

    fun setPbVisibility(b: Boolean) {
        if (b) {
            activityMainBinding.pb.visibility = View.VISIBLE
            activityMainBinding.rvGallery.visibility = View.GONE
        } else {
            activityMainBinding.pb.visibility = View.GONE
            activityMainBinding.rvGallery.visibility = View.VISIBLE
        }
    }
}