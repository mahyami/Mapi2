package com.example.mapi.data.remote.services

import com.example.mapi.data.remote.RemotePlace
import com.example.mapi.data.remote.models.Coordinate

interface IPlacesApiService {
    suspend fun getPlaceDetails(id: String): Result<RemotePlace>
    
    suspend fun searchNearbyPlacesToGetPlaceId(
        coordinate: Coordinate,
        radius: Double = 1.0,
        maxResults: Int = 1
    ): Result<List<String>>
}