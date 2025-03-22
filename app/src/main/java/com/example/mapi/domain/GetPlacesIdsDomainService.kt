package com.example.mapi.domain

import javax.inject.Inject

class GetPlacesIdsDomainService @Inject constructor() {

    // For the talk, this is interesting to say how we hacked to get the place id
    operator fun invoke(): List<String> {
        TODO("parse the json")
//        return parseCSVApplicationService.getLocations(
//            context = context,
//        ).mapNotNull { location ->
//            extractFtIdFromUrl(location.url)
//        }
    }

    private fun extractFtIdFromUrl(url: String): String? {
        val sequence = "!4m2!3m1!1s"
        val index = url.indexOf(sequence)

        return if (index != -1) {
            val startIndex = index + sequence.length
            val endIndex = url.indexOf("/", startIndex).takeIf { it != -1 } ?: url.length
            url.substring(startIndex, endIndex)
        } else {
            null
        }
    }
}