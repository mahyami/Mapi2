package com.example.mapi.domain

import android.content.res.Resources
import com.example.mapi.R
import com.example.mapi.data.remote.Coordinate
import com.example.mapi.data.remote.GetLatLongFromTakeoutUrlService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class GetPlacesCoordinatesDomainService @Inject constructor(
    private val getLatLongFromTakeoutUrlService: GetLatLongFromTakeoutUrlService
) {

    suspend operator fun invoke(resources: Resources): List<Coordinate> {
        return getUrlsFromJsonFile(resources)
            .mapNotNull {
                getLatLongFromTakeoutUrlService.getCoordinatesFromUrl(it)
            }
    }

    private fun getUrlsFromJsonFile(resources: Resources): List<String> {
        val inputStream = resources.openRawResource(R.raw.saved)

        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val placeListType = object : TypeToken<List<Place>>() {}.type
        val places: List<Place> = Gson().fromJson(jsonString, placeListType)

        inputStream.close()

        return places.map { it.url }
    }

    private data class Place(
        @SerializedName("Title") val title: String,
        @SerializedName("URL") val url: String,
    )
}

