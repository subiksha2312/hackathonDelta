package com.example.hackathondelta

import android.net.Uri

data class Photos(
    var photoId: String,
    var height: String,
    var displayName: String,
    var dateTaken: String,
    var title: String,
    var relativePath: String,
    var photoUri: Uri
)

