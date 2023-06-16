package com.example.projectcapstone.detailscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projectcapstone.databinding.ActivityDetailBinding
import com.example.projectcapstone.response.Camera

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO_URL = "extra_photo"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val action = supportActionBar
        action!!.title = "Detail Batik Classification"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var name = ""
        var photoUrl = ""
        var description = ""
        var city = ""

        val cameraResponse = intent.getSerializableExtra("cameraResponse") as? Camera
        if (cameraResponse != null) {
            println(cameraResponse)
            name = cameraResponse.data.name
            photoUrl = cameraResponse.data.imageUrl
            description = cameraResponse.data.description
            city = cameraResponse.data.city
        } else {
            /* TODO : Please handle if camera Response null or error happened*/
        }

        binding.username.text = name
        binding.scanSubtitle.text = city
        Glide.with(this)
            .load(photoUrl)
            .skipMemoryCache(true)
            .into(binding.storyView)
        binding.caption.text = description

    }

}