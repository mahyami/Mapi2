package com.example.mapi.domain

import com.example.mapi.data.gemini.IGeminiService
import com.example.mapi.data.local.PlacesDao
import com.example.mapi.data.remote.models.Coordinate
import com.example.mapi.data.remote.services.IPlacesApiService
import com.example.mapi.data.toLocalPlace
import com.example.mapi.domain.models.PlaceIdResult
import com.example.mapi.domain.models.PlaceResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val placesApiService: IPlacesApiService,
    private val placesDao: PlacesDao,
    private val geminiService: IGeminiService,
) {

    fun getPlacesIdsFromCoordinates(coordinates: List<Coordinate>): Flow<PlaceIdResult> = flow {
        coordinates.map { coordinate ->
            placesApiService.searchNearbyPlacesToGetPlaceId(coordinate)
                .map {
                    emit(PlaceIdResult.Success(it))
                }
                .onFailure {
                    emit(PlaceIdResult.Failure)
                }
        }
    }

    fun getPlacesDetails(ids: List<String>): Flow<Boolean> = flow {
        coroutineScope {
            val deferredResults = ids.map { id ->
                async { getPlaceDetail(id) }
            }
            
            val results = deferredResults.map { it.await() }
            val successCount = results.count { it is PlaceResult.Success }
            
            emit(successCount > 0)
        }
    }

    suspend fun sendMessage(prompt: String) = geminiService.sendMessage(prompt)

    private suspend fun getPlaceDetail(id: String): PlaceResult {
        return placesApiService.getPlaceDetails(id)
            .map { place ->
                placesDao.insertPlace(place.toLocalPlace())
                PlaceResult.Success(place.id)
            }
            .getOrElse { PlaceResult.Failure }
    }

    suspend fun getPlacesCount() = placesDao.getPlacesCount()
}