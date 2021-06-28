package com.example.altokepeapp.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.altokepeapp.R
import java.io.IOException

class GlideLoader (val context: Context){

    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            //Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) //URI de la imagen
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder_user)  // A defaul place Holder id Image is failed to load.
                .into(imageView) // yhe wiew in which the image will be loaded.
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

}