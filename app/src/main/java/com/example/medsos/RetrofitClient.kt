import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.GsonBuilder

class RetrofitClient {
    private val retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/") // Ensure the base URL ends with '/'
            .addConverterFactory(ScalarsConverterFactory.create()) // For handling non-JSON responses
            .addConverterFactory(GsonConverterFactory.create(gson)) // For JSON conversion
            .client(client)
            .build()
    }

    // To get the API service instance
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}
