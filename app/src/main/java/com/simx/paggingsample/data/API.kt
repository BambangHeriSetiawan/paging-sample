package com.simx.paggingsample.data

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.simx.paggingsample.BuildConfig
import com.simx.paggingsample.data.discover.ResponseMovies
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by simx on 14,August,2019
 */
interface API {

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("discover/movie")
    fun discoverAsync(@Query("api_key")apiKey:String,
                      @Query("page")page:Int,
                      @Query("year")year:Int
    ): Deferred<ResponseMovies>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("search/movie")
    fun searchAsync(@Query("api_key")apiKey:String,
                      @Query("page")page:Int,
                      @Query("year")year:Int?,
                      @Query("query")query:String?
    ): Deferred<ResponseMovies>

    /*#########################*/
    object Factory {
        /**
         * Config GSON
         * @return
         */
        private val gson: Gson
            get() {
                val gsonBuilder = GsonBuilder()
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                return gsonBuilder.create()
            }

        fun create(base_url: String): API {
            return getRetrofitConfig(base_url).create(API::class.java)
        }

        private fun getRetrofitConfig(base_url: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client())
                .build()
        }

        /**
         * Config OkhttpClient and interceptions
         * @return
         */
        private fun client(): OkHttpClient {
            return OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(levelInterceptor())
                .build()
        }
        private fun levelInterceptor(): HttpLoggingInterceptor {
            var loging = HttpLoggingInterceptor()
            return if (BuildConfig.DEBUG){
                loging.setLevel(HttpLoggingInterceptor.Level.BODY)
            }else {
                loging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            }
        }
    }
}