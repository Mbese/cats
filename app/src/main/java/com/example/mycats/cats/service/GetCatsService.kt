package com.example.mycats.cats.service

import com.example.mycats.cats.data.Cat
import retrofit2.Response
import retrofit2.http.GET

interface GetCatsService {
    @GET("v1/images/search?include_breeds=true&limit=10&page=1")
    suspend fun getCats(): Response<List<Cat>>
}