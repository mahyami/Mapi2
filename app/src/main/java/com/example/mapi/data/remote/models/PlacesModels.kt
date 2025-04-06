package com.example.mapi.data.remote.models

import kotlinx.serialization.Serializable

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class SearchNearbyRequest(
    val includedTypes: List<String>,
    val maxResultCount: Int,
    val locationRestriction: LocationRestriction
)

@Serializable
data class LocationRestriction(
    val circle: Circle
)

@Serializable
data class Circle(
    val center: Center,
    val radius: Double
)

@Serializable
data class Center(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class SearchNearbyResponse(
    val places: List<Place> = emptyList()
)

@Serializable
data class Place(
    val id: String
)
