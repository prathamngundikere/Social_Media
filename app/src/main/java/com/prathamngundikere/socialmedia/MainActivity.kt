package com.prathamngundikere.socialmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        bottomNav = findViewById(R.id.bottomNavigationView)

        // Default fragment
        replaceFragment(PhotoFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_photos -> replaceFragment(PhotoFragment())
                R.id.nav_post -> replaceFragment(PostFragment())
                R.id.nav_chat -> replaceFragment(ChatFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun hideBottomNav() {
        bottomNav.translationY = bottomNav.height.toFloat()
    }

    fun showBottomNav() {
        bottomNav.translationY = 0f
    }

}