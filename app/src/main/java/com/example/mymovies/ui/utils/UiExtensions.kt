package com.example.mymovies.ui.utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

fun <T> Fragment.viewLifecycleBinding(initialise: () -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
            this@viewLifecycleBinding
                .viewLifecycleOwner
                .lifecycle
                .removeObserver(this)
            super.onDestroy(owner)
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>
        ): T {
            return binding ?: initialise().also {
                binding = it
                this@viewLifecycleBinding.viewLifecycleOwner.lifecycle.addObserver(this)
            }
        }
    }