package com.example.projectcapstone.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dummyartikeldata(
    // Satrio
    val id: String,
    val name: String,
    val description: String,
    val detail: String,
    val photo: String
): Parcelable