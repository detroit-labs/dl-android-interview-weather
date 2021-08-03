package com.detroitlabs.weather.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.isVisible(visible: Boolean?) {
    visible?.let {
        visibility = if (it) VISIBLE else GONE
    }
}