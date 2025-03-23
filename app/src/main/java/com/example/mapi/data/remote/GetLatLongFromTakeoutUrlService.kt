package com.example.mapi.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern
import javax.inject.Inject

class GetLatLongFromTakeoutUrlService @Inject constructor() {
    private val client = OkHttpClient()
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
                        println("URL pattern not found in the response")
                        return@withContext ""
                    }
                } else {
                    println("Request failed with status code: ${response.code}")
                    return@withContext ""
                }
            } catch (e: Exception) {
                println("Error making request: ${e.message}")
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

    suspend fun getCoordinatesFromUrl(url: String): Coordinate? {
        val mapsUrl = getMapsUrlWithLatAndLong(url)
        return extractLatLong(mapsUrl)
    }
}