package com.example.mycats.cats.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.mycats.api.ApiClient
import com.example.mycats.cats.data.Cat
import com.example.mycats.cats.data.CatDao
import com.example.mycats.cats.service.GetCatsService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatsRepo(
        private val dao: CatDao,
        private val apiService: GetCatsService = ApiClient.getCatService()
) {
    val cat = MutableLiveData<List<Cat>>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("", "$exception handled !")
    }

    init {
        CoroutineScope(Dispatchers.IO + handler).launch {
            kotlin.runCatching {
                getCats()
            }
        }
    }

    @WorkerThread
    suspend fun getCats() {
        var catsData = dao.getCats().value
        if (catsData.isNullOrEmpty()) {
            catsData = apiService.getCats().body()
            if (!catsData.isNullOrEmpty()) {
                cat.postValue(catsData)
                dao.insert(catsData)
            }
        } else {
            cat.postValue(catsData)
        }
    }
}