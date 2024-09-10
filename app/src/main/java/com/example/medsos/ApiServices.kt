package com.example.medsos


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

interface ApiServices {

    @POST("/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

}