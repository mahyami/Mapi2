package com.example.mapi.data.remote.services

import com.example.mapi.data.remote.models.Coordinate

interface ICoordinatesService {
    suspend fun getCoordinatesFromUrl(url: String): Coordinate?
}
