package com.example.medsos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class signup : AppCompatActivity() {

    lateinit var db: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var signup_user: AppCompatButton = findViewById(R.id.signup_user)
        var name: EditText = findViewById(R.id.name_input)
        var username: EditText = findViewById(R.id.username_input)
        var email: EditText = findViewById(R.id.email_input)
        var password: EditText = findViewById(R.id.pass_input)
        val connection: connection = connection()
        db = UserDBHelper(this)

        signup_user.setOnClickListener {

            if(name.text.length == 0){
                name.setError("This field must not null")
            }else if(username.text.length == 0){
                username.setError("This field must not null")
            }else if(email.text.length == 0){
                email.setError("This field must not null")
            }else if(password.text.length == 0){
                password.setError("This field must not null")
            }else{

                val users = UserModel(0, name.text.toString(), username.text.toString(), email.text.toString(), password.text.toString())
                db.registrationUser(users)
                Toast.makeText(this, "Registrasi berhasil!, silahkan login", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, login::class.java))

//                val jsonObject = JSONObject().apply {
//                    put("name", name.text.toString())
//                    put("username", username.text.toString())
//                    put("email", email.text.toString())
//                    put("password", password.text.toString())
//                }
//
//                lifecycleScope.launch {
//
//                    val result = postRequest(connection.connection + "user/create", jsonObject, null)
//
//                    result.fold(
//                        onSuccess = {
//                            response -> var jsonObject2 = JSONObject(response)
//
//                            if(jsonObject2.getString("status").equals("success")){
//                                Toast.makeText(applicationContext, "Daftar berhasil, silahkan login!", Toast.LENGTH_SHORT).show()
//                                startActivity(Intent(applicationContext, login::class.java))
//                            }
//                        },
//                        onFailure = {
//                            error -> error.printStackTrace()
//                        }
//                    )
//                }
            }
        }

        var close: TextView = findViewById(R.id.signup_close)

        close.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }



    }
}