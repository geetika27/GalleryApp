package com.dsrise.kotlinkoin.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsrise.kotlinkoin.R
import com.dsrise.kotlinkoin.cache.MYLRUCache
import com.dsrise.kotlinkoin.databinding.ItemViewBinding
import com.dsrise.kotlinkoin.model.ImageDetail
import com.dsrise.kotlinkoin.utils.Constants
import com.dsrise.kotlinkoin.utils.ImageHelper
import java.net.HttpURLConnection
import java.net.URL

class ImageViewAdapter(val list: List<ImageDetail>, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {
    private var mList: List<ImageDetail>? = ArrayList()

    private val mItemClickListener: ImageViewAdapter.ItemClickListener
        get() = itemClickListener


    fun updateItems(items: List<ImageDetail>?) {
        this.mList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val binding: ItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view, parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(mList!![position])
    }

    override fun getItemCount(): Int {

        return mList!!.size ?: 0
    }


    inner class ImageViewHolder(itemViewBinding: ItemViewBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        private var itemViewBinding: ItemViewBinding? = null

        init {
            this.itemViewBinding = itemViewBinding
        }

        fun onBind(imageDetail: ImageDetail) {
            itemViewBinding?.let {
                it.textViewRecyclerView.text = "NAME : ${imageDetail.name}\nPRICE : ${imageDetail.price}"
                showOrDownloadImage(it.imageViewRecyclerView, imageDetail?.image_urls[0])
            }
            itemViewBinding?.root?.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition, mList!![adapterPosition])
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int, imageDetail: ImageDetail)
    }

    private fun showOrDownloadImage(ivImage: ImageView, url: String) {
        if (MYLRUCache.getBitmapFromCache(url) != null) {
            ivImage.setImageBitmap(MYLRUCache.getBitmapFromCache(url))
        } else {


            try {
                HttpImageReqTask(object : ImageHelper {
                    override fun onImageDownload(result: Bitmap?) {
                        try {
                            MYLRUCache.addBitmapToCache(url, result!!)
                            ivImage.setImageBitmap(result)
                        } catch (e: Exception) {
                            Toast.makeText(
                                ivImage.context,
                                "Error: Internet Connection Not Working <> ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }).execute(url)
            } catch (e: Exception) {
                Toast.makeText(
                    ivImage.context,
                    "Error: Internet Connection Not Working",
                    Toast.LENGTH_LONG
                ).show()
            }
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
                Log.d(Constants.APP_LOGGER_TAG, "doInBackground: Image URL Downloading : \n $imageUrl")
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
}