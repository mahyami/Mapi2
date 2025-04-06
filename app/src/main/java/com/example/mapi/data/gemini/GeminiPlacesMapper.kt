package com.example.mapi.data.gemini

import com.example.mapi.data.local.LocalPlace
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import org.json.JSONObject

class GeminiPlacesMapper {

    fun buildSystemPrompt(places: List<LocalPlace>): String {
        val placesJson = places.map { place ->
            """
                {
                    "displayName": "${place.displayName}",
                    "name": "${place.name}",
                    "rating": "${place.rating}",
                    "types": "${place.types}",
                    "address": "${place.formattedAddress}",
                    "rating": ${place.rating},
                    "googleMapsUri": "${place.googleMapsUri}",
                    "reviews": "${place.reviews}",
                    "businessStatus": "${place.businessStatus}",
                    "priceLevel": "${place.priceLevel}",
                    "generativeSummary": "${place.generativeSummary}",
                    "googleMapsLinks": "${place.googleMapsLinks}",
                    "takeout": "${place.takeout}",
                    "delivery": "${place.delivery}",
                    "dineIn": "${place.dineIn}",
                    "curbsidePickup": "${place.curbsidePickup}",
                    "reservable": "${place.reservable}",
                    "servesBreakfast": "${place.servesBreakfast}",
                    "servesLunch": "${place.servesLunch}",
                    "servesDinner": "${place.servesDinner}",
                    "servesBeer": "${place.servesBeer}",
                    "servesWine": "${place.servesWine}",
                    "servesBrunch": "${place.servesBrunch}",
                    "servesVegetarianFood": "${place.servesVegetarianFood}",
                    "outdoorSeating": "${place.outdoorSeating}",
                    "liveMusic": "${place.liveMusic}",
                    "menuForChildren": "${place.menuForChildren}",
                    "servesCocktails": "${place.servesCocktails}",
                    "servesDessert": "${place.servesDessert}",
                    "servesCoffee": "${place.servesCoffee}",
                    "goodForChildren": "${place.goodForChildren}",
                    "allowsDogs": "${place.allowsDogs}",
                    "restroom": "${place.restroom}",
                    "goodForGroups": "${place.goodForGroups}",
                    "goodForWatchingSports": "${place.goodForWatchingSports}",
                    "pureServiceAreaBusiness": "${place.pureServiceAreaBusiness}",
                    "editorialSummary": "${place.editorialSummary}",
                    "acceptsCreditCards": "${place.paymentOptions.acceptsCreditCards}",
                    "acceptsDebitCards": "${place.paymentOptions.acceptsDebitCards}",
                    "acceptsCash": "${place.paymentOptions.acceptsCash}",
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
            **$placesJson:** This variable holds the list of my saved places with their characteristics.
            
            **Response Format:**
            
            For each suggested place, include:
            
            *   The name of the place.
            *   The Google Maps URL from the place object in my `$placesJson` list.
        """.trimIndent()
    }

    fun buildResponseSchema(): Schema<JSONObject> = Schema(
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