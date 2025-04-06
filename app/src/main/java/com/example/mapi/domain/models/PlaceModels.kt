package com.example.mapi.domain.models

sealed interface PlaceResult {
    data class Success(val placeId: String) : PlaceResult
    data object Failure : PlaceResult
}

sealed interface PlaceIdResult {
    data class Success(val ids: List<String>) : PlaceIdResult
    data object Failure : PlaceIdResult
}
