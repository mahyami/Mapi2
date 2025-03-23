package com.example.mapi.data.remote

import com.example.mapi.BuildConfig.PLACES_API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MapsHttpClient @Inject constructor() {
    fun getClient() = HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(Logging) { logger = Logger.SIMPLE }

        defaultRequest {
            url {
                host = "places.googleapis.com"
                protocol = URLProtocol.HTTPS
                header("X-Goog-Api-Key", PLACES_API_KEY)
            }
        }
    }
}