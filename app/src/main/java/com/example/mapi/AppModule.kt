package com.example.mapi

import android.content.Context
import androidx.room.Room
import com.example.mapi.data.gemini.GeminiPlacesMapper
import com.example.mapi.data.gemini.GeminiService
import com.example.mapi.data.gemini.IGeminiService
import com.example.mapi.data.local.PlacesDao
import com.example.mapi.data.remote.GetLatLongFromTakeoutUrlService
import com.example.mapi.data.remote.PlacesApiService
import com.example.mapi.data.remote.PlacesHttpClient
import com.example.mapi.data.remote.services.ICoordinatesService
import com.example.mapi.data.remote.services.IPlacesApiService
import com.google.mapi.data.local.MapiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMapiDatabase(@ApplicationContext appContext: Context): MapiDatabase {
        return Room.databaseBuilder(
            appContext,
            MapiDatabase::class.java,
            "mapi_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlaceDao(mapiDatabase: MapiDatabase): PlacesDao = mapiDatabase.placeDao()

    @Provides
    @Singleton
    fun providePlacesHttpClient(): PlacesHttpClient = PlacesHttpClient()

    @Provides
    @Singleton
    fun providePlacesApiService(placesHttpClient: PlacesHttpClient): IPlacesApiService =
        PlacesApiService(placesHttpClient)

    @Provides
    @Singleton
    fun provideCoordinatesService(): ICoordinatesService =
        GetLatLongFromTakeoutUrlService()

    @Provides
    @Singleton
    fun provideGeminiPlacesMapper(): GeminiPlacesMapper = GeminiPlacesMapper()

    @Provides
    @Singleton
    fun provideGeminiService(
        placesDao: PlacesDao,
        geminiPlacesMapper: GeminiPlacesMapper
    ): IGeminiService = GeminiService(placesDao, geminiPlacesMapper)
}