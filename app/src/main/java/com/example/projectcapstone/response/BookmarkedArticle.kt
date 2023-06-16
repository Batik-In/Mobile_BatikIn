package com.example.projectcapstone.response

import com.google.gson.annotations.SerializedName

data class BookmarkedArticle (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: BookmarkedArticleData
)

data class BookmarkedArticleData (
    @field:SerializedName("isBookmark")
    val isBookmark: Boolean
)