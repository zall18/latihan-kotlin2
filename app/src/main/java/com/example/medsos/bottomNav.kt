package com.example.medsos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class bottomNav : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        bottomNav = findViewById(R.id.bottomnavigation)


        bottomNav.setOnItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    replaceFragment(home())
                    true
                }
                R.id.search -> {
                    replaceFragment(search())
                    true
                }
                R.id.post -> {
                    replaceFragment(addpost())
                    true
                }
                R.id.profile -> {
                    replaceFragment(profile())
                    true
                }
                else -> false
            }
        }
        replaceFragment(home())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}