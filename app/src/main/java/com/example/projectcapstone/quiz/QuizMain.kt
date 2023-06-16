package com.example.projectcapstone.quiz

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.projectcapstone.R
import com.example.projectcapstone.camera.CameraActivity
import com.example.projectcapstone.databinding.ActivityHomeBinding
import com.example.projectcapstone.databinding.ActivityQuizMainBinding
import com.example.projectcapstone.login.LoginActivity
import com.example.projectcapstone.main.HomeActivity
import com.example.projectcapstone.preferences.UserPreference
import com.google.android.material.bottomnavigation.BottomNavigationView

class QuizMain : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {
    private lateinit var pref: UserPreference
    private lateinit var binding: ActivityQuizMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.menu.getItem(0).isChecked= true


        playAnimation()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(this@QuizMain, HomeActivity::class.java))
            }
            R.id.camera -> {
                startActivity(Intent(this@QuizMain, CameraActivity::class.java))
            }
            R.id.logout -> {
                AlertDialog.Builder(this@QuizMain).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.logout_validation))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        pref.logOut()
                        val intent = Intent(this@QuizMain, LoginActivity::class.java)
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