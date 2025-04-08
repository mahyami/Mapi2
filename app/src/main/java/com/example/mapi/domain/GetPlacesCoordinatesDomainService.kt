package com.example.mapi.domain

import android.content.res.Resources
import android.util.Log
import com.example.mapi.R
import com.example.mapi.data.remote.models.Coordinate
import com.example.mapi.data.remote.services.ICoordinatesService
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class GetPlacesCoordinatesDomainService @Inject constructor(
    private val coordinatesService: ICoordinatesService
) {

    suspend operator fun invoke(resources: Resources): List<Coordinate> {
        return getUrlsFromFile(resources)
            .mapNotNull {
                coordinatesService.getCoordinatesFromUrl(it)
            }
    }

    /**
     * Copyright goes to Piyush 🚀
     * https://github.com/poush
     * */
    private fun getUrlsFromFile(resources: Resources): List<String> {
        val inputStream = resources.openRawResource(R.raw.food)
        val urls = mutableListOf<String>()

        try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use { bufferedReader ->
                bufferedReader.forEachLine { line ->
                    // Split by comma, but handle potential quoted values in CSV
                    val contents = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())

                    // Skip header row or malformed lines
                    if (contents.size < 3 || contents[0].equals("Title", ignoreCase = true)) {
                        return@forEachLine
                    }

                    // Extract URL - typically in the 3rd column (index 2)
                    val urlString = contents[2].trim().removeSurrounding("\"")
                    if (urlString.startsWith("https://www.google.com/maps")) {
                        urls.add(urlString)
                    }
                }
            }
        } finally {
            try {
                inputStream.close()
            } catch (e: Exception) {
                Log.e("GetPlacesService", "Error closing input stream: ${e.message}")
            }
        }

        return urls
    }
}
