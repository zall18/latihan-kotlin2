package com.example.medsos

import RetrofitClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import okhttp3.Callback
import org.json.JSONObject

class login : AppCompatActivity() {

    lateinit var session: SharedPreferences
    lateinit var db: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = getSharedPreferences("session", Context.MODE_PRIVATE)
        var editor = session.edit()
        var connection: connection = connection()
        var first: EditText = findViewById(R.id.first_input)
        var password: EditText = findViewById(R.id.password_input)
        var login_user: AppCompatButton = findViewById(R.id.login_user)
        var retrofitClient = RetrofitClient(this)
        db = UserDBHelper(this)

        login_user.setOnClickListener {

            if(first.text.length == 0){
                first.setError("This field must not null")
            }else if(password.text.length == 0){
                password.setError("This field must not null")
            }else{
                val requestQueue = Volley.newRequestQueue(applicationContext)
                var stringRequest = object : StringRequest(Request.Method.POST, "https://fakestoreapi.com/auth/login", Response.Listener {
                    response ->
                    try {
                        val jsonObject = JSONObject(response)
                        Log.d("response", "onCreate: $jsonObject")
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    error -> error.printStackTrace()
                }){
                    override fun getBodyContentType(): String {
                        return "application/x-www-form-urlencoded;charset=UTF-8"
                    }

                    override fun getParams(): MutableMap<String, String>? {
                        val param: MutableMap<String, String> = HashMap()
                        param["username"] = first.text.toString()
                        param["password"] = password.text.toString()
                        return param
                    }

                }
                requestQueue.cache.clear()
                requestQueue.add(stringRequest)
            }

        }

        var close: TextView = findViewById(R.id.login_close)

        close.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}