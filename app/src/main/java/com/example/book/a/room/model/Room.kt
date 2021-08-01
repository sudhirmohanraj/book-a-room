package com.example.book.a.room.model

import android.graphics.Bitmap

data class Room(
    val name:String,
    val spots:Int,
    val thumbnail:String,
    var imageBitmap: Bitmap? = null
)
