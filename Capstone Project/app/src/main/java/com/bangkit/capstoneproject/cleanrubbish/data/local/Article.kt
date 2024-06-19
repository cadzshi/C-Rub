package com.bangkit.capstoneproject.cleanrubbish.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val photo: Int
): Parcelable
