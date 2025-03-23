package com.example.mapi.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import kotlinx.serialization.Serializable
import javax.inject.Inject


class MapsApiService @Inject constructor(
    private val mapsHttpClient: MapsHttpClient
) {

    suspend fun getPlaceDetails(id: String): Result<RemotePlace> {
        return mapsHttpClient.getClient()
            .get {
                url {
                    header("X-Goog-FieldMask", "*")
                    appendPathSegments(DETAILS_PATH)
                    appendPathSegments(id)
                }
            }
    }

    suspend fun searchNearbyPlacesToGetPlaceId(
        coordinate: Coordinate,
        radius: Double = 10.0,
        maxResults: Int = 1
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

            val response = mapsHttpClient.getClient()
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

