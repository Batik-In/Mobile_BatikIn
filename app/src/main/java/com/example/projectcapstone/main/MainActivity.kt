package com.example.projectcapstone.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.projectcapstone.R
import com.example.projectcapstone.databinding.ActivityHomeBinding
import com.example.projectcapstone.login.LoginActivity
import com.example.projectcapstone.preferences.UserPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        setupView()
        loginCheck()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun loginCheck() {
        if (pref.getUser().token != ""){
            startActivity(Intent(this, HomeActivity::class.java))
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
                finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.logout_validation))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        pref.logOut()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
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
        return super.onOptionsItemSelected(item)
    }
}