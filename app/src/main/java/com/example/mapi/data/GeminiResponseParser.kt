package com.example.mapi.data

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import javax.inject.Inject

@Serializable
data class ResponseItem(
    val name: String,
    val url: String
)

@Serializable
data class ResponseWrapper(
    val response: List<ResponseItem>
)

class GeminiResponseParser @Inject constructor() {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    /**
     * Parse JSON string to ResponseWrapper object
     * @param jsonString The JSON string to parse
     * @return ResponseWrapper containing the parsed data
     * @throws SerializationException if parsing fails
     */
    fun parseResponse(jsonString: String): ResponseWrapper {
        return json.decodeFromString(jsonString)
    }

    /**
     * Parse JSON string and extract only the list of ResponseItems
     * @param jsonString The JSON string to parse
     * @return List of ResponseItem objects
     * @throws SerializationException if parsing fails
     */
    fun parseResponseItems(jsonString: String): List<ResponseItem> {
        return parseResponse(jsonString).response
    }
}