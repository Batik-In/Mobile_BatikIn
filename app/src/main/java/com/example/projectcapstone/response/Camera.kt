package com.example.projectcapstone.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Camera (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: CameraData

) : Serializable

data class CameraData (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("city")
    val city: String,

) : Serializable