package com.example.meuprimeiroapp.ui

import android.widget.ImageView
import com.example.meuprimeiroapp.R
import com.squareup.picasso.Picasso

/**
 * Extension function for ImageView to load an image from a URL with Picasso.
 * It also applies a circular transformation and sets placeholder and error images.
 *
 * @param url The URL of the image to load.
 */
fun ImageView.loadUrl(url: String) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ic_download)
        .error(R.drawable.ic_error)
        .transform(CircleTransform())
        .into(this)
}