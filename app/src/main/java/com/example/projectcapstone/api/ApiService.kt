package com.example.projectcapstone.api

import androidx.room.Insert
import com.example.projectcapstone.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @Multipart
    @POST("classification")
    fun scan(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<Camera>

    @GET("articles/isBookmark/{id}")
    fun isBookmarked(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<BookmarkedArticle>

    @POST("articles/bookmark/{id}")
    fun addToBookmark(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<Bookmarkdetail>

    @DELETE("articles/bookmark/{id}")
    fun deleteFromBookmark(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<Bookmarkdetail>
}