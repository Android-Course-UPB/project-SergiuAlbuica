package com.app.shopping.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface ProductApiService {
    @GET("cgi/search.pl?search_simple=1&json=1&page_size=5")
    suspend fun searchProducts(@Query("search_terms") query: String): Response<ProductSearchResponse>

    companion object {
        private const val BASE_URL = "https://world.openfoodfacts.org/"

        fun create(): ProductApiService {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductApiService::class.java)
        }
    }
}
