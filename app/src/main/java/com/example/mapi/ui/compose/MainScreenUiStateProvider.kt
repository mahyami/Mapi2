package com.example.mapi.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.mapi.ui.PlaceUiModel
import com.example.mapi.ui.PlacesUiState

class MainScreenUiStateProvider : PreviewParameterProvider<PlacesUiState> {
    override val values = sequenceOf(
        PlacesUiState.Sync.Initial,
        PlacesUiState.Sync.Loading,
        PlacesUiState.Gemini.Loading,
        PlacesUiState.Gemini.PlacesRecommendation(PlacesUiState.Gemini.Places.NotFound),
        PlacesUiState.Gemini.PlacesRecommendation(
            PlacesUiState.Gemini.Places.Found(
                listOf(
                    PlaceUiModel(
                        name = "Place 1",
                        url = "",
                    ),
                    PlaceUiModel(
                        name = "Place 2 has a very veery long name",
                        url = "",
                    )
                )
            )
        ),
        PlacesUiState.Gemini.PlacesRecommendation(
            PlacesUiState.Gemini.Places.Found(
                emptyList()
            )
        )
    )
}