package com.example.projectcapstone.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectcapstone.api.ApiConfig
import com.example.projectcapstone.response.Camera
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CameraViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResponse = MutableLiveData<Camera>()
    val uploadResponse: LiveData<Camera> = _uploadResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun uploadStory(token: String, image: File, description: String, callback: (Camera?) -> Unit) {
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file", image.name, requestImageFile
        )

        _isLoading.value = true

        val bearer = "Bearer $token";

        val client = ApiConfig.getApiService()
        client.scan(bearer, imageMultipart).enqueue(object : Callback<Camera> {
            override fun onResponse(call: Call<Camera>, response: Response<Camera>) {
                println("MESSAGE")
                println(response.body())
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uploadResponse.value = response.body()
                    callback(response.body())
                } else {
                    Log.e(TAG, "onFailure 1 : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Camera>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure 2 : ${t.message}")
                println(t.message)
                _error.value = "Gagal upload foto: ${t.message}"
            }
        })
    }
}