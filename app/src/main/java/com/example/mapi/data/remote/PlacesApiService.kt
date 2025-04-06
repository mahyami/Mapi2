package com.example.mapi.data.remote

import com.example.mapi.data.remote.models.Center
import com.example.mapi.data.remote.models.Circle
import com.example.mapi.data.remote.models.Coordinate
import com.example.mapi.data.remote.models.LocationRestriction
import com.example.mapi.data.remote.models.SearchNearbyRequest
import com.example.mapi.data.remote.models.SearchNearbyResponse
import com.example.mapi.data.remote.services.IPlacesApiService
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import javax.inject.Inject

class PlacesApiService @Inject constructor(
    private val placesHttpClient: PlacesHttpClient
) : IPlacesApiService {

    override suspend fun getPlaceDetails(id: String): Result<RemotePlace> {
        return placesHttpClient.getClient()
            .get {
                url {
                    header("X-Goog-FieldMask", "*")
                    appendPathSegments(DETAILS_PATH)
                    appendPathSegments(id)
                }
            }
    }

    override suspend fun searchNearbyPlacesToGetPlaceId(
        coordinate: Coordinate,
        radius: Double,
        maxResults: Int
    ): Result<List<String>> {
        return try {
            val requestBody = SearchNearbyRequest(
                includedTypes = listOf(),
                maxResultCount = maxResults,
                locationRestriction = LocationRestriction(
                    circle = Circle(
                        center = Center(
                            latitude = coordinate.latitude,
                            longitude = coordinate.longitude
                        ),
                        radius = radius
                    )
                )
            )

            val response = placesHttpClient.getClient()
                .post {
                    url {
                        appendPathSegments(SEARCH_NEARBY_PATH)
                    }
                    header("Content-Type", "application/json")
                    header("X-Goog-FieldMask", "places.id")
                    setBody(requestBody)
                }
                .body<SearchNearbyResponse>()

            Result.success(response.places.map { it.id })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    companion object {
        private const val DETAILS_PATH = "v1/places/"
        private const val SEARCH_NEARBY_PATH = "v1/places:searchNearby"
    }
}