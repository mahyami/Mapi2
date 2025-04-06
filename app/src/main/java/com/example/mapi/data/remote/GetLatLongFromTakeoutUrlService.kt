package com.example.mapi.data.remote

import android.util.Log
import com.example.mapi.data.remote.models.Coordinate
import com.example.mapi.data.remote.services.ICoordinatesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern
import javax.inject.Inject

class GetLatLongFromTakeoutUrlService @Inject constructor() : ICoordinatesService {
    private val client = OkHttpClient()
    private val TAG = "GetLatLongService"

    override suspend fun getCoordinatesFromUrl(url: String): Coordinate? {
        val mapsUrl = getMapsUrlWithLatAndLong(url)
        return extractLatLong(mapsUrl)
    }

    private suspend fun getMapsUrlWithLatAndLong(url: String): String =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""

                    // Define a regex pattern to match the desired URL format
                    val pattern = Pattern.compile(
                        "https://www\\.google\\.com/maps/preview/place/[^,]+,[^,]+,[^@]+@[\\d.]+,[\\d.]+"
                    )

                    val matcher = pattern.matcher(responseBody)
                    if (matcher.find()) {
                        val extractedUrl = matcher.group(0)
                        return@withContext extractedUrl ?: ""
                    } else {
                        Log.w(TAG, "URL pattern not found in the response")
                        return@withContext ""
                    }
                } else {
                    Log.e(TAG, "Request failed with status code: ${response.code}")
                    return@withContext ""
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error making request: ${e.message}")
                e.printStackTrace()
                return@withContext ""
            }
        }

    private fun extractLatLong(url: String): Coordinate? {
        val regex =
            "https://www\\.google\\.com/maps/preview/place/[^@]+@([\\d.]+),([\\d.]+)".toRegex()
        val matchResult = regex.find(url)
        return if (matchResult != null) {
            val (lat, long) = matchResult.destructured
            Coordinate(lat.toDouble(), long.toDouble())
        } else {
            null
        }
    }
}