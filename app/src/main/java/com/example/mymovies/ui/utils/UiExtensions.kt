package com.example.mymovies.ui.utils

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

inline fun <reified T> AppCompatActivity.startActivity(intentExtras: Bundle? = null) {
    Intent(this, T::class.java).apply {
        intentExtras?.let {
            putExtras(it)
        }
    }.also(::startActivity)
}

fun ImageView.loadImageFromUrl(imageUrl: String) {
    Glide.with(this).load(imageUrl).into(this)
}
