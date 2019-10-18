package com.coroutinerequester.app.di

import android.preference.PreferenceManager
import com.coroutinerequester.app.BuildConfig
import com.coroutinerequester.app.data.DataManager
import com.coroutinerequester.app.data.api.ApiInterface
import com.coroutinerequester.app.data.restaurants.RestaurantDataSrc
import com.coroutinerequester.app.data.restaurants.RestaurantsRepo
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // ApiInterface
    single {

        Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(ApiInterface::class.java)
    }

    // OkHttpClient
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val builder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
        builder.build()
    }

    single { DataManager(get()) }

    // default pref
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    factory { RestaurantsRepo(get()) }
    factory { RestaurantDataSrc(get()) }
}
