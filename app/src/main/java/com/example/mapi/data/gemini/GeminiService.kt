package com.example.mapi.data.gemini

import com.example.mapi.BuildConfig.GEMINI_API_KEY
import com.example.mapi.data.local.PlacesDao
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val placesDao: PlacesDao,
) {

    suspend fun sendMessage(userPrompt: String): GenerateContentResponse {
        val model = GenerativeModel(
            modelName = "gemini-2.0-flash-lite",
            apiKey = GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.3f
                responseMimeType = "application/json"
                responseSchema = buildResponseSchema()
            },
            systemInstruction = buildSystemInstruction()
        )

        return model
            .startChat()
            .sendMessage(userPrompt)
    }

    private suspend fun buildSystemInstruction(): Content {
        val modelKnowledgeSource = buildModelKnowledgeSourceFromUserPlaces()
        return content("system") {
            parts = mutableListOf(
                TextPart(modelKnowledgeSource)
            )
        }
    }

    private suspend fun buildModelKnowledgeSourceFromUserPlaces(): String {
        // TODO:: clean the data
        val getPlaces = placesDao.getAllPlaces().map {
            """
                {
                    "displayName": "${it.displayName}",
                    "name": "${it.name}",
                    "rating": "${it.rating}",
                    "types": "${it.types}",
                    "address": "${it.formattedAddress}",
                    "rating": ${it.rating},
                    "googleMapsUri": "${it.googleMapsUri}",
                    "reviews": "${it.reviews}",
                    "businessStatus": "${it.businessStatus}",
                    "priceLevel": "${it.priceLevel}",
                    "generativeSummary": "${it.generativeSummary}",
                    "googleMapsLinks": "${it.googleMapsLinks}",
                    "takeout": "${it.takeout}",
                    "delivery": "${it.delivery}",
                    "dineIn": "${it.dineIn}",
                    "curbsidePickup": "${it.curbsidePickup}",
                    "reservable": "${it.reservable}",
                    "servesBreakfast": "${it.servesBreakfast}",
                    "servesLunch": "${it.servesLunch}",
                    "servesDinner": "${it.servesDinner}",
                    "servesBeer": "${it.servesBeer}",
                    "servesWine": "${it.servesWine}",
                    "servesBrunch": "${it.servesBrunch}",
                    "servesVegetarianFood": "${it.servesVegetarianFood}",
                    "outdoorSeating": "${it.outdoorSeating}",
                    "liveMusic": "${it.liveMusic}",
                    "menuForChildren": "${it.menuForChildren}",
                    "servesCocktails": "${it.servesCocktails}",
                    "servesDessert": "${it.servesDessert}",
                    "servesCoffee": "${it.servesCoffee}",
                    "goodForChildren": "${it.goodForChildren}",
                    "allowsDogs": "${it.allowsDogs}",
                    "restroom": "${it.restroom}",
                    "goodForGroups": "${it.goodForGroups}",
                    "goodForWatchingSports": "${it.goodForWatchingSports}",
                    "pureServiceAreaBusiness": "${it.pureServiceAreaBusiness}",
                    "editorialSummary": "${it.editorialSummary}",
                    "acceptsCreditCards": "${it.paymentOptions.acceptsCreditCards}",
                    "acceptsDebitCards": "${it.paymentOptions.acceptsDebitCards}",
                    "acceptsCash": "${it.paymentOptions.acceptsCash}",
                }
            """.trimIndent()
        }
        return """
                Your an assistant to help users find the perfect café or restaurant or
                any place they are craving from their saved google places list.
                The user will describe what s.he is looking for and you will find it.
                   
                You will be provided with a list of my saved places, and I will then describe what I'm looking for in a place.

                You should only pick a place from the list that you get from the getPlaces tool. You should not suggest any other place.
                If you can't find a place that matches my description, return an empty list.
                **$getPlaces:** This variable holds the list of my saved places with their characteristics.
                
                **Response Format:**
                
                For each suggested place, include:
                
                *   The name of the place.
                *   The Google Maps URL from the place object in my `$getPlaces` list.
                """.trimIndent()
    }

    /** OpenAPI Specification
    {
    "type": "object",
    "properties": {
    "response": {
    "type": "array",
    "description": "Array of items with name and URL",
    "items": {
    "type": "object",
    "description": "Item with name and URL",
    "required": ["name", "url"],
    "properties": {
    "name": {
    "type": "string",
    "description": "The name of the item"
    },
    "url": {
    "type": "string",
    "description": "The URL associated with the item"
    }
    }
    }
    }
    }
    }

     */

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