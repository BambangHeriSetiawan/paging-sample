package com.simx.paggingsample.data

import com.simx.paggingsample.BuildConfig

/**
 * Created by simx on 14,August,2019
 */
object ApiRequests {
    suspend fun discover(page:Int, year:Int)  = API.Factory.create(BuildConfig.BASE_URL).discoverAsync(BuildConfig.API_KEY,page,year).await()
    suspend fun search(page:Int, year:Int,query:String)  = API.Factory.create(BuildConfig.BASE_URL).searchAsync(BuildConfig.API_KEY,page,year,query).await()
}