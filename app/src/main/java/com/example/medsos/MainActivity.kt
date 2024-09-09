package com.example.medsos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var login_button: AppCompatButton = findViewById(R.id.login_button)

        login_button.setOnClickListener {
            startActivity(Intent(applicationContext, login::class.java))
        }

        var signup_button: AppCompatButton = findViewById(R.id.signup_button)

        signup_button.setOnClickListener {
            startActivity(Intent(applicationContext, signup::class.java))
        }
    }
}