package com.example.mymovies.ui.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T> AppCompatActivity.startActivity(intentExtras: Bundle? = null) {
    Intent(this, T::class.java).apply {
        intentExtras?.let {
            putExtras(it)
        }
    }.also(::startActivity)
}