package com.example.himanshi2002.project2.Utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.himanshi2002.project2.R
import java.io.IOException

class GlideLoader(val context: Context)
{
    fun loadUserPicture(imageUri : Uri, imageView: ImageView)
    {
        try
        {
            Glide.with(context)
                .load(Uri.parse(
                    imageUri.toString()))
                .centerCrop()
                .placeholder(R.drawable.appsign)
                .into(imageView)

        }
        catch(e:IOException)
        {
            e.printStackTrace()
        }
    }

}