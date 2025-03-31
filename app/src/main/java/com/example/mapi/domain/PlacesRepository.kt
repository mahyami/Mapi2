package com.example.mapi.domain

import android.util.Log
import com.example.mapi.data.toLocalPlace
import com.example.mapi.data.gemini.GeminiService
import com.example.mapi.data.local.PlacesDao
import com.example.mapi.data.remote.Coordinate
import com.example.mapi.data.remote.PlacesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesApiService: PlacesApiService,
    private val placesDao: PlacesDao,
    private val geminiService: GeminiService,
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
        val sizeOfPlaces = ids.size
        val detailsInserted: MutableList<PlaceResult.Success> = mutableListOf()
        ids.mapIndexed { index, id ->
            getPlaceDetails(id).collect {
                if (index == sizeOfPlaces - 1) {
                    emit(detailsInserted.isNotEmpty())
                }
                if (it is PlaceResult.Success) {
                    detailsInserted.add(it)
                }
            }
        }
    }

    suspend fun sendMessage(prompt: String) = geminiService.sendMessage(prompt)

    private fun getPlaceDetails(id: String): Flow<PlaceResult> = flow {
        placesApiService.getPlaceDetails(id)
            .map {
                Log.d("MAHYA:: PlacesRepository", "getPlaceDetails : $it")
                placesDao.insertPlace(it.toLocalPlace())
                emit(PlaceResult.Success(it.id))
            }
            .onFailure {
                emit(PlaceResult.Failure)
            }
    }

    sealed interface PlaceResult {
        data class Success(val placeId: String) : PlaceResult
        data object Failure : PlaceResult
    }

    sealed interface PlaceIdResult {
        data class Success(val ids: List<String>) : PlaceIdResult
        data object Failure : PlaceIdResult
    }

    suspend fun getPlacesCount() = placesDao.getPlacesCount()
}
