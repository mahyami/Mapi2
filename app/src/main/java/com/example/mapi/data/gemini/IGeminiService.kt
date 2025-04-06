package com.example.mapi.data.gemini

import com.google.ai.client.generativeai.type.GenerateContentResponse

interface IGeminiService {
    suspend fun sendMessage(userPrompt: String): GenerateContentResponse
}
