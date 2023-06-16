package com.example.projectcapstone.artikel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.projectcapstone.R
import com.example.projectcapstone.api.ApiConfig
import com.example.projectcapstone.preferences.UserPreference
import com.example.projectcapstone.response.Bookmarkdetail
import com.example.projectcapstone.response.BookmarkedArticle
import com.example.projectcapstone.response.dummyartikeldata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArtikelActivity : AppCompatActivity() {
    private lateinit var pref: UserPreference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)
        pref = UserPreference(this)
        val actionBar = supportActionBar
        actionBar!!.title = "Detail Artikel"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val article = intent.getParcelableExtra<dummyartikeldata>("key_fitur") as dummyartikeldata
        val title : TextView = findViewById(R.id.titleartikel)
        val subtitle : TextView = findViewById(R.id.subtitleartikel)
        val description : TextView = findViewById(R.id.caption)
        val ivDetailPhoto : ImageView = findViewById(R.id.detail_image)

        title.text = article.name
        description.text = article.description
        subtitle.text = article.detail

        Glide.with(this)
            .load(article.photo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(ivDetailPhoto)

        val toggleBookmark: ToggleButton = findViewById(R.id.toggle_bookmark)
        val articleId = article.id
        checkBookmarkStatus(articleId, pref.getUser().token, toggleBookmark)

        toggleBookmark.setOnClickListener{
            println("Toggle Bookmark clicked ..")
            bookmarkAction(articleId, pref.getUser().token, toggleBookmark)
        }
    }

    private fun checkBookmarkStatus(articleId: String, token: String, toggleBookmark: ToggleButton) {
        /* Perform the GET request to check if article is bookmarked */

        val client = ApiConfig.getApiService()
        val bearer = "Bearer $token"

        client.isBookmarked(bearer, articleId).enqueue(object : Callback<BookmarkedArticle> {
            override fun onResponse(
                call: Call<BookmarkedArticle>,
                response: Response<BookmarkedArticle>
            ) {
                if (response.isSuccessful) {
                    val bookmarkDetail = response.body()
                    if (bookmarkDetail != null) {
                        toggleBookmark.isChecked = bookmarkDetail.data.isBookmark
                    }
                }
            }

            override fun onFailure(call: Call<BookmarkedArticle>, t: Throwable) {
                // Handle network request failure
                println("ERROR on isBookmark check")
                println(t)
            }
        })
    }

    private fun bookmarkAction (articleId: String, token: String, toggleBookmark: ToggleButton) {
        val isBookmarked: Boolean = toggleBookmark.isChecked

        val client = ApiConfig.getApiService()
        val bearer = "Bearer $token"

        if(isBookmarked) {
            /* Handle remove from bookmark */
            client.deleteFromBookmark(bearer, articleId).enqueue(object : Callback<Bookmarkdetail> {
                override fun onResponse(call: Call<Bookmarkdetail>, response: Response<Bookmarkdetail>) {
                    if (response.isSuccessful) {
                        println("Delete bookmark response : ")
                        println(response.message())
                    }
                }

                override fun onFailure(call: Call<Bookmarkdetail>, t: Throwable) {
                    // Handle network request failure
                    println("Error on delete bookmark !!")
                    println(t)
                }
            })
        } else {
            /* Handle add to bookmark */
            client.addToBookmark(bearer, articleId).enqueue(object : Callback<Bookmarkdetail> {
                override fun onResponse(call: Call<Bookmarkdetail>, response: Response<Bookmarkdetail>) {
                    if (response.isSuccessful) {
                        println("Add bookmark response : ")
                        println(response.message())
                    }
                }

                override fun onFailure(call: Call<Bookmarkdetail>, t: Throwable) {
                    println("Error on add bookmark !!")
                    println(t)
                }
            })
        }
    }

}