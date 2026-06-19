package com.anderson.financeapp

import android.content.Context
import android.media.MediaPlayer

fun playSuccessSound(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.success)
    mediaPlayer.setOnCompletionListener {
        it.release()
    }
    mediaPlayer.start()
}