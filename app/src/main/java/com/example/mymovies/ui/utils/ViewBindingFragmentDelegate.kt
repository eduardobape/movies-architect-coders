package com.example.mymovies.ui.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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
            // Return the backing property if it's set, or initialise
            return binding ?: initialise().also {
                binding = it
                this@viewLifecycleBinding.viewLifecycleOwner.lifecycle.addObserver(this)
            }
        }
    }




