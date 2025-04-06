package com.example.mapi.data.gemini

import com.example.mapi.BuildConfig.GEMINI_API_KEY
import com.example.mapi.data.local.PlacesDao
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor(
    private val placesDao: PlacesDao,
    private val geminiPlacesMapper: GeminiPlacesMapper
) : IGeminiService {

    override suspend fun sendMessage(userPrompt: String): GenerateContentResponse {
        val model = GenerativeModel(
            modelName = "gemini-2.0-flash-lite",
            apiKey = GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.3f
                responseMimeType = "application/json"
                responseSchema = geminiPlacesMapper.buildResponseSchema()
            },
            systemInstruction = getSystemInstruction()
        )

        return model
            .startChat()
            .sendMessage(userPrompt)
    }

    private suspend fun getSystemInstruction(): Content {
        val modelKnowledgeSource = buildModelKnowledgeSourceFromUserPlaces()
        return content("system") {
            parts = mutableListOf(
                TextPart(modelKnowledgeSource)
            )
        }
    }

    private suspend fun buildModelKnowledgeSourceFromUserPlaces(): String {
        val places = placesDao.getAllPlaces()
        return geminiPlacesMapper.buildSystemPrompt(places)
    }
}