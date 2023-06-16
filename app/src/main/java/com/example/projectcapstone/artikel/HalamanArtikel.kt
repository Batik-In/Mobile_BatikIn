package com.example.projectcapstone.artikel

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcapstone.R
import com.example.projectcapstone.adapter.ListArtikelAdapter
import com.example.projectcapstone.camera.CameraActivity
import com.example.projectcapstone.databinding.ActivityHalamanArtikelBinding
import com.example.projectcapstone.favorite.FavoritebookmarkActivity
import com.example.projectcapstone.login.LoginActivity
import com.example.projectcapstone.main.HomeActivity
import com.example.projectcapstone.preferences.UserPreference
import com.example.projectcapstone.response.dummyartikeldata
import com.google.android.material.bottomnavigation.BottomNavigationView

class HalamanArtikel : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var pref: UserPreference
    private lateinit var binding: ActivityHalamanArtikelBinding

    private lateinit var rvFitur: RecyclerView
    private val list = ArrayList<dummyartikeldata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHalamanArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvFitur = findViewById(R.id.rv_fitur)
        rvFitur.setHasFixedSize(true)

        pref = UserPreference(this)
        val actionBar = supportActionBar
        actionBar!!.title = "Artikel tentang Batik"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.menu.getItem(0).isChecked= true

        list.addAll(getListArtikel())
        showRecyclerList()
        playAnimation()
    }

    private fun getListArtikel(): ArrayList<dummyartikeldata> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataDetail = resources.getStringArray(R.array.data_detail)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listArtikel = ArrayList<dummyartikeldata>()
        for (i in dataName.indices) {
            val artikel = dummyartikeldata("", dataName[i], dataDescription[i], dataDetail[i], "https://storage.googleapis.com/batikin-bucket/2_b1ec3888f547af71_Geblek-Renteng5.jpg")
            listArtikel.add(artikel)
        }
        return listArtikel
    }

    private fun showRecyclerList() {
        rvFitur.layoutManager = LinearLayoutManager(this)
        val listArtikelAdapter = ListArtikelAdapter(list, pref)
        rvFitur.adapter = listArtikelAdapter

        listArtikelAdapter.setOnItemClickCallback(object : ListArtikelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: dummyartikeldata) {
                println("CLICKED");
                showSelectedArtikel(data)
                val artikellist = dummyartikeldata(
                    data.id,
                    data.name,
                    data.detail,
                    data.name,
                    data.photo
                )
                println(artikellist)
                val intent = Intent(this@HalamanArtikel, DetailArtikelActivity::class.java).apply {
                    putExtra("key_fitur", artikellist)
                }
                println("INTENT")
                println(intent)
                startActivity(intent)
            }
        })
    }
    private fun showSelectedArtikel(artikel: dummyartikeldata) {
        Toast.makeText(this, "Kamu memilih " + artikel.name, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageViewartikel, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bookmark, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                startActivity(Intent(this, FavoritebookmarkActivity::class.java))
                return true
            }
            else -> return true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home ->{
                startActivity(Intent(this@HalamanArtikel, HomeActivity::class.java))
            }
            R.id.camera -> {
                startActivity(Intent(this@HalamanArtikel, CameraActivity::class.java))
            }
            R.id.logout -> {
                AlertDialog.Builder(this@HalamanArtikel).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.logout_validation))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        pref.logOut()
                        val intent = Intent(this@HalamanArtikel, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton(getString(R.string.no)) { _, _ -> }
                    create()
                    show()
                }
            }
        }
        return true
    }
}