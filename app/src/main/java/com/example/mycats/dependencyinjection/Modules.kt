package com.example.mycats.dependencyinjection

import androidx.room.Room
import com.example.mycats.api.ApiClient
import com.example.mycats.cats.repository.CatsRepo
import com.example.mycats.cats.service.GetCatsService
import com.example.mycats.cats.viewmodel.CatsViewModel
import com.example.mycats.constants.BASE_URL
import com.example.mycats.data.AppDatabase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel {
        CatsViewModel(get())
    }
}

val repoModule = module {
    single {
        CatsRepo(get(), get())
    }
}

val apiModule = module {
    fun provideApi(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    single { provideApi(get()) }
}

val persistenceModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "cat.db").build()
    }
    single { get<AppDatabase>().catsDao() }
}

val retrofitModule = module {
    fun retrofit(baseUrl: String) = Retrofit.Builder()
            .callFactory(OkHttpClient.Builder().build())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun okHttp() = OkHttpClient.Builder()
            .build()

    single {
        okHttp()
    }
    single {
        retrofit(BASE_URL)
    }
    single {
        get<Retrofit>().create(GetCatsService::class.java)
    }
}