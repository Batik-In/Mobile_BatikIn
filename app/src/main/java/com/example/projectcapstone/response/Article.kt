package com.example.projectcapstone.response

import com.google.gson.annotations.SerializedName

data class Article (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<ArticleData>

)

data class ArticleData (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("categoryId")
    val categoryId: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("subtitle")
    val subtitle: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)