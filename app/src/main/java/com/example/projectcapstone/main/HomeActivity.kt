package com.example.projectcapstone.main

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
import com.example.projectcapstone.artikel.HalamanArtikel
import com.example.projectcapstone.R
import com.example.projectcapstone.adapter.ListFiturAdapter
import com.example.projectcapstone.camera.CameraActivity
import com.example.projectcapstone.databinding.ActivityHomeBinding
import com.example.projectcapstone.favorite.FavoritebookmarkActivity
import com.example.projectcapstone.fitur.Deskripsi.Companion.EXTRA_FITUR
import com.example.projectcapstone.login.LoginActivity
import com.example.projectcapstone.preferences.UserPreference
import com.example.projectcapstone.response.Fitur
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var pref: UserPreference
    private lateinit var binding: ActivityHomeBinding


    private lateinit var rvFitur: RecyclerView
    private val list = ArrayList<Fitur>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.menu.getItem(0).isChecked= true

        playAnimation()

        rvFitur = findViewById(R.id.rv_fitur)
        rvFitur.setHasFixedSize(true)

        list.addAll(getListFitur())
        showRecyclerList()
    }

    private fun getListFitur(): ArrayList<Fitur> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataDetail = resources.getStringArray(R.array.data_detail)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listFitur = ArrayList<Fitur>()
        for (i in dataName.indices) {
            val artis = Fitur(dataName[i], dataDescription[i], dataDetail[i], dataPhoto.getResourceId(i, -1))
            listFitur.add(artis)
        }
        return listFitur
    }

    private fun showRecyclerList() {
        rvFitur.layoutManager = LinearLayoutManager(this)
        val listFiturAdapter = ListFiturAdapter(list)
        rvFitur.adapter = listFiturAdapter
        listFiturAdapter.setOnItemClickCallback(object : ListFiturAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Fitur) {
                showSelectedArtis(data)
                val fiturlist = Fitur(
                    data.description,
                    data.detail,
                    data.name,
                    data.photo
                )

                val intent = Intent(this@HomeActivity, HalamanArtikel::class.java)
                intent.putExtra(EXTRA_FITUR, fiturlist)
                startActivity(intent)

            }
        })
    }
    private fun showSelectedArtis(fitur: Fitur) {
        Toast.makeText(this, "Kamu memilih " + fitur.name, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
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
                val i = Intent(this, FavoritebookmarkActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home->{
                startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
            }
            R.id.camera -> {
                startActivity(Intent(this@HomeActivity, CameraActivity::class.java))
            }
            R.id.logout -> {
                AlertDialog.Builder(this@HomeActivity).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.logout_validation))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        pref.logOut()
                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
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