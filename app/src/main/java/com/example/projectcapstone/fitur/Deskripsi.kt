@file:Suppress("DEPRECATION")

package com.example.projectcapstone.fitur


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.projectcapstone.R
import com.example.projectcapstone.response.Fitur


class Deskripsi : AppCompatActivity() {

    companion object {
        const val EXTRA_FITUR = "extra_fitur"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        val actionBar = supportActionBar
        actionBar!!.title = "Deskripsi"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val dataArtis = intent.getParcelableExtra<Fitur>("key_Fitur") as Fitur

        val tvDetailName : TextView = findViewById(R.id.titleartikel)
        val tvDetailDescription :TextView = findViewById(R.id.caption)
        val ivDetailPhoto : ImageView = findViewById(R.id.detail_image)

        tvDetailName.text = dataArtis.name
        tvDetailDescription.text = dataArtis.description
        ivDetailPhoto.setImageResource(dataArtis.photo)
    }


}