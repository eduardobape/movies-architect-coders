package com.example.mymovies.ui.utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

var View.visible: Boolean
    get() = isVisible
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
