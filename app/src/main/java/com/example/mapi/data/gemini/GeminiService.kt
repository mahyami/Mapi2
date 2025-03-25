package com.example.mapi.data.gemini

import com.example.mapi.BuildConfig.GEMINI_API_KEY
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.example.mapi.data.local.PlacesDao
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val placesDao: PlacesDao,
) {

    suspend fun sendMessage(prompt: String): GenerateContentResponse {
        val model = GenerativeModel(
            modelName = "gemini-2.0-flash-lite",
            apiKey = GEMINI_API_KEY,
            generationConfig = buildGenerationConfig(),
            systemInstruction = buildSystemInstruction()
        )

        return model
            .startChat()
            .sendMessage(prompt)
    }

    private suspend fun buildSystemInstruction(): Content {
        val modelKnowledgeSource = buildModelKnowledgeSourceFromUserPlaces()
        return content("system") {
            parts = mutableListOf(
                TextPart(
                    """
                    Your an assistant to help users find the perfect café or restaurant or
                    any place you are craving from your saved google places list.
                    The user will describe what s.he is looking for and I will find it.
                    """
                ),
                TextPart(modelKnowledgeSource)
            )
        }
    }

    private suspend fun buildModelKnowledgeSourceFromUserPlaces(): String {
        // TODO:: clean the data
        val places = placesDao.getAllPlaces().take(10).toString()
        return """
                You will be provided with a list of my saved places, and I will then describe what I'm looking for in a place.

                Your task is to suggest up to three places from my list that best fit my criteria. Please prioritize matches within the city I specify (if applicable).
                
                If a perfect match isn't available, tell me that but suggest up to three alternative places from my list that are as close as possible to my request.
                
                **Important considerations:**
                
                *   **$places:** This variable holds the list of my saved place objects, each including the name, Google Maps URL, and an `openNow` field.
                *   **"Open Now" Requests:** If my request includes a requirement for the place to be open now, only consider places where the `openNow` field is true. If nothing is open at the moment based on `openNow` in my provided place data, state there's nothing currently open from your list, ignore the `openNow` requirement.
                
                **Response Format:**
                
                For each suggested place, include:
                
                *   The name of the place.
                *   The Google Maps URL from the place object in my `$places` list.
                """.trimIndent()
    }

    private fun buildGenerationConfig() = generationConfig {
        temperature = 0.5f
        topK = 8
        responseMimeType = "application/json"
        responseSchema = buildResponseSchema()
    }

    private fun buildResponseSchema() = Schema(
        name = "root",
        description = "Root schema object",
        type = FunctionType.OBJECT,
        properties = mapOf(
            "response" to Schema(
                name = "response",
                description = "Array of items with name and URL",
                type = FunctionType.ARRAY,
                items = Schema(
                    name = "responseItem",
                    description = "Item with name and URL",
                    type = FunctionType.OBJECT,
                    required = listOf("name", "url"),
                    properties = mapOf(
                        "name" to Schema(
                            name = "name",
                            description = "The name of the item",
                            type = FunctionType.STRING
                        ),
                        "url" to Schema(
                            name = "url",
                            description = "The URL associated with the item",
                            type = FunctionType.STRING
                        )
                    )
                )
            )
        )
    )
}