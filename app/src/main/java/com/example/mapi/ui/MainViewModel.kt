package com.example.mapi.ui

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapi.data.GeminiResponseParser
import com.example.mapi.domain.GetPlacesCoordinatesDomainService
import com.example.mapi.domain.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPlacesCoordinates: GetPlacesCoordinatesDomainService,
    private val placesRepository: PlacesRepository,
    private val geminiResponseParser: GeminiResponseParser,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlacesUiState>(
        PlacesUiState.Sync.Initial
    )
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        viewModelScope.launch {
            placesRepository.getPlacesCount().let { count ->
                if (count != 0 && _uiState.value !is PlacesUiState.Sync.Loading) {
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(
                            PlacesUiState.Gemini.Places.Found(
                                emptyList()
                            )
                        )
                    }
                }
            }
        }
    }

    fun onSyncClicked(resources: Resources) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Sync.Loading
            }

            val coordinates = getPlacesCoordinates(resources)
            Log.d("MAHYA:: MainViewModel", "getPlacesDetails : coordinates $coordinates")

            placesRepository.getPlacesIdsFromCoordinates(
                coordinates
            ).map { placesIdReceived ->
                when (placesIdReceived) {
                    PlacesRepository.PlaceIdResult.Failure -> {
                        Log.d(
                            "MAHYA:: MainViewModel",
                            "nothing received!! "
                        )
                        null
                    }

                    is PlacesRepository.PlaceIdResult.Success -> {
                        Log.d(
                            "MAHYA:: MainViewModel",
                            "getPlacesDetails : placesIds ${placesIdReceived.ids}"
                        )
                        placesRepository.getPlacesDetails(placesIdReceived.ids)
                    }
                }
            }.collect {
                it?.collectLatest {
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(
                            PlacesUiState.Gemini.Places.Found(
                                emptyList()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun emitToastEvent(reason: String) {
        Log.e("MainViewModel::emitToastEvent ", "REASON:: $reason")
        _uiEvent.update {
            UiEvent.ShowToast("Failed to get data! Try again")
        }
    }

    fun onSubmitButtonClicked(prompt: String) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Gemini.Loading
            }
            placesRepository.sendMessage(prompt).let { response ->
                response.text?.let {
                    parseGeminiResultToUiModel(it)
                }?.let { places ->
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(
                            if (places.isEmpty()) {
                                PlacesUiState.Gemini.Places.NotFound
                            } else {
                                PlacesUiState.Gemini.Places.Found(places)
                            }
                        )
                    }
                }
            }
        }
    }

    fun onOpenMapsClicked(url: String) {
        _uiEvent.update {
            UiEvent.OpenBrowser(url)
        }
    }

    private val _uiEvent = MutableStateFlow<UiEvent>(UiEvent.Default)
    val uiEvent: StateFlow<UiEvent> = _uiEvent

    sealed interface UiEvent {
        data class OpenBrowser(val url: String) : UiEvent
        data object Default : UiEvent
        data class ShowToast(val message: String) : UiEvent
    }

    private fun parseGeminiResultToUiModel(response: String): List<PlaceUiModel> {
        val items = geminiResponseParser.parseResponseItems(response)
        return items.map {
            PlaceUiModel(
                name = it.name,
                url = it.url
            )
        }
    }
}