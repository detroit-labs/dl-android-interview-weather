package com.detroitlabs.weather.data.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun providesBaseUrl(): HttpUrl = "https://api.openweathermap.org/data/2.5/".toHttpUrl()
}