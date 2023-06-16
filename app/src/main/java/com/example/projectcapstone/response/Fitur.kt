package com.example.projectcapstone.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fitur(
    val name: String,
    val description: String,
    val detail: String,
    val photo: Int
): Parcelable