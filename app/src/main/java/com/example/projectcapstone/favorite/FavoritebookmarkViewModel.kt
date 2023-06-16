package com.example.projectcapstone.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projectcapstone.api.ApiService
import com.example.projectcapstone.response.Article
import retrofit2.Call

class FavoritebookmarkViewModel(application: Application): AndroidViewModel(application) {

    private var ArtikelDao: ApiService? = null

    fun getFavoriteUser(): Call<Article>? {
        return ArtikelDao?.getartikel()
    }

}

private fun ApiService?.getartikel(): Call<Article>? {
    TODO("Not yet implemented")
}
