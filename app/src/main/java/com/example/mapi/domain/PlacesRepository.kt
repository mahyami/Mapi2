package com.example.mapi.domain

import com.example.mapi.data.toLocalPlace
import com.example.mapi.data.gemini.GeminiService
import com.example.mapi.data.local.PlacesDao
import com.example.mapi.data.remote.MapsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val mapsApiService: MapsApiService,
    private val placesDao: PlacesDao,
    private val geminiService: GeminiService,
) {

    fun getPlacesDetails(ids: List<String>): Flow<Boolean> = flow {
        val sizeOfPlaces = ids.size
        val detailsInserted: MutableList<PlaceResult.Success> = mutableListOf()
        ids
            .mapIndexed { index, id ->
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
        mapsApiService.getPlaceDetails(id)
            .map {
                placesDao.insertPlace(it.toLocalPlace())
                emit(PlaceResult.Success(it.result.place_id))
            }
            .onFailure {
                emit(PlaceResult.Failure)
            }
    }

    sealed interface PlaceResult {
        data class Success(val placeId: String) : PlaceResult
        data object Failure : PlaceResult
    }

    suspend fun getPlacesCount() = placesDao.getPlacesCount()
}
