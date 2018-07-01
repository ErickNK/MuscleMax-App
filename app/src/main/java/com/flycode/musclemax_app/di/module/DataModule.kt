package com.flycode.musclemax_app.di.module

import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.flycode.musclemax_app.data.Config
import com.flycode.musclemax_app.data.db.Database
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.data.network.AuthService
import com.flycode.musclemax_app.data.network.TempService
import com.flycode.musclemax_app.data.network.UserService
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
open class DataModule{

    @Provides
    @Singleton
    fun provideDatabaseDefinition(): DatabaseDefinition {
        return FlowManager.getDatabase(Database::class.java)
    }

    @Named("default_user")
    @Provides
    @Singleton
    fun provideDefaultUser(): User {
        val user = SQLite.select()
                .from(User::class.java)
                .querySingle()

        return user ?: User()
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache {
        return Cache(context.cacheDir, (100 * 1024 * 1024).toLong())//100 MB cache
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("Main", Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
            cache: Cache,
            @Named("authentication_interceptor") authenticationInterceptor: Interceptor,
            @Named("network_interceptor") networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(networkInterceptor)
                //.addInterceptor(httpLoggingInterceptor)
                //.addNetworkInterceptor(stethoInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Named("authentication_interceptor")
    @Provides
    @Singleton
    fun authenticationInterceptor(
            sharedPreferences: SharedPreferences
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()

        if (!sharedPreferences.getString("token",null).isNullOrEmpty()) {

            val builder = request
                    .newBuilder()
                    .header("Authorization", sharedPreferences.getString("token", null))
                    .header("X-Requested-With","XMLHttpRequest")

            request = builder.build()
        }
        chain.proceed(request)
    }

    @Named("network_interceptor")
    @Provides
    @Singleton
    fun provideIntercepter(
            sharedPreferences: SharedPreferences
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()
        val builder = request
                .newBuilder()
                .header("X-Requested-With","XMLHttpRequest")

            request = builder.build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Config.BACKEND_ENDPOINT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(
            retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
            retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideTempService(
            retrofit: Retrofit
    ): TempService = retrofit.create(TempService::class.java)

}
