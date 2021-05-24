package com.example.mycats.api

import com.example.mycats.cats.service.GetCatsService
import com.example.mycats.constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private fun getRetrofitInstance(): Retrofit.Builder {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
        }

        fun getCatService(): GetCatsService {
            return getRetrofitInstance().build().create(GetCatsService::class.java)
        }
    }
}