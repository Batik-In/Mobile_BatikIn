package com.example.projectcapstone.response

import com.google.gson.annotations.SerializedName

data class detailArtikel (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ArticleData

)
