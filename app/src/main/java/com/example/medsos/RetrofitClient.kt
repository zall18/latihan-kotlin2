// RetrofitClient.kt
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class RetrofitClient(private val context: Context) {
    private val retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com") // replace with your base URL
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    suspend fun login(username: String, password: String): String {

        val service = retrofit.create(ApiInterface::class.java)

        val loginRequest = LoginRequest(username, password)

        val call = service.login(loginRequest)

        val response = call.execute()

        return response.body()!!

    }

    data class LoginRequest(val username: String, val password: String)

    private interface ApiInterface {
        @POST("/auth/login")
        fun login(@Body loginRequest: LoginRequest): Call<String>
    }
}