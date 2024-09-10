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
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call

class signup : AppCompatActivity() {

    lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        session = getSharedPreferences("session", Context.MODE_PRIVATE)
        var editor = session.edit()
        var connection: connection = connection()
        var first: EditText = findViewById(R.id.first_input)
        var password: EditText = findViewById(R.id.password_input)
        var login_user: AppCompatButton = findViewById(R.id.login_user)

        login_user.setOnClickListener {

            if(first.text.length == 0){
                first.setError("This field must not null")
            }else if(password.text.length == 0){
                password.setError("This field must not null")
            }else{

                var retrofitClient = RetrofitClient()
                var apiService = retrofitClient.create(ApiServices::class.java)

                var loginRequest = LoginRequest(first.text.toString(), password.text.toString())

                apiService.loginUser(loginRequest).enqueue(object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {
                        val loginResponse = response.body()

                        Log.d("response", "onResponse: " + loginResponse?.token)
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("response", "onFailure: $t")
                    }

                })

//                val requestQueue = Volley.newRequestQueue(applicationContext)
//                var stringRequest = object : StringRequest(Request.Method.POST, "https://fakestoreapi.com/auth/login", Response.Listener {
//                    response ->
//                    try {
//                        val jsonObject = JSONObject(response)
//                        Log.d("response", "onCreate: $jsonObject")
//                    }catch (e: Exception){
//                        e.printStackTrace()
//                    }
//                }, Response.ErrorListener {
//                    error -> error.printStackTrace()
//                }){
//                    override fun getBodyContentType(): String {
//                        return "application/x-www-form-urlencoded;charset=UTF-8"
//                    }
//
//                    override fun getParams(): MutableMap<String, String>? {
//                        val param: MutableMap<String, String> = HashMap()
//                        param["username"] = first.text.toString()
//                        param["password"] = password.text.toString()
//                        return param
//                    }
//
//                }
//                requestQueue.cache.clear();
//                requestQueue.add(stringRequest)
            }

        }

        var close: TextView = findViewById(R.id.login_close)

        close.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }
}