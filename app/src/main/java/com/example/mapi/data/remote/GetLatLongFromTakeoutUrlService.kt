package com.example.mapi.data.remote

import android.util.Log
import com.example.mapi.data.remote.models.Coordinate
import com.example.mapi.data.remote.services.ICoordinatesService
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.util.regex.Pattern
import javax.inject.Inject

class GetLatLongFromTakeoutUrlService @Inject constructor(
    private val takeoutHttpClient: TakeoutHttpClient
) : ICoordinatesService {
    private val TAG = "GetLatLongService"

    override suspend fun getCoordinatesFromUrl(url: String): Coordinate? {
        val mapsUrl = getMapsUrlWithLatAndLong(url)
        return extractLatLong(mapsUrl)
    }

    private suspend fun getMapsUrlWithLatAndLong(url: String): String {
        return try {
            val response = takeoutHttpClient.getClient().get(url)
            val responseBody = response.bodyAsText()

            Log.d(TAG, "Response Body: $responseBody")
            // Define a regex pattern to match the desired URL format
            val pattern = Pattern.compile(
                "https://www\\.google\\.com/maps/preview/place/[^,]+,[^,]+,[^@]+@[\\d.]+,[\\d.]+"
            )

            val matcher = pattern.matcher(responseBody)
            if (matcher.find()) {
                matcher.group(0) ?: ""
            } else {
                Log.w(TAG, "URL pattern not found in the response")
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching URL: ${e.message}")
            e.printStackTrace()
            ""
        }
    }

    private fun extractLatLong(url: String): Coordinate? {
        val regex =
            "https://www\\.google\\.com/maps/preview/place/[^@]+@([\\d.]+),([\\d.]+)"
                .toRegex()
        val matchResult = regex.find(url)
        return if (matchResult != null) {
            val (lat, long) = matchResult.destructured
            Coordinate(lat.toDouble(), long.toDouble())
        } else {
            null
        }
    }
}