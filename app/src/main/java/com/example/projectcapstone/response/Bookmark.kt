package com.example.projectcapstone.response

import com.google.gson.annotations.SerializedName

data class Bookmark (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<BookmarkData>
)

data class BookmarkData (
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("articleId")
    val articleId: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)