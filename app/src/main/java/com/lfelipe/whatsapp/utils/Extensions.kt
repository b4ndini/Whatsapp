package com.lfelipe.whatsapp.utils

import android.net.Uri
import android.widget.ImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

fun ImageView.loadSvg(url: String?) {
    GlideToVectorYou
        .init()
        .with(this.context)
        //.setPlaceHolder(R.drawable.loading, R.drawable.actual)
        .load(Uri.parse(url), this)

}