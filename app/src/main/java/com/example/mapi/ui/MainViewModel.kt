package com.example.mapi.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapi.domain.GetPlacesIdsDomainService
import com.example.mapi.domain.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPlacesIds: GetPlacesIdsDomainService,
    private val placesRepository: PlacesRepository,
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

    private fun onTakeoutCsvDataReceived(context: Context, isSuccess: Boolean) {
        viewModelScope.launch {
            if (isSuccess) {
                getPlacesDetails(context)
            } else {
                _uiState.update {
                    PlacesUiState.Sync.Initial
                }
                emitToastEvent("Takeout data not received")
            }
        }
    }

    private fun getPlacesDetails(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Sync.Loading
            }

            placesRepository.getPlacesDetails(getPlacesIds())
                .collectLatest { placesDetailsReceived ->
                    if (placesDetailsReceived) {
                        _uiState.update {
                            PlacesUiState.Gemini.PlacesRecommendation(
                                PlacesUiState.Gemini.Places.Found(
                                    emptyList()
                                )
                            )
                        }
                        Log.d("MAHYA:: MainViewModel", "Places details received")
                    } else {
                        _uiState.update {
                            PlacesUiState.Sync.Initial
                        }
                        emitToastEvent("All places details not received")
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

    fun onSyncButtonClicked() {
        TODO("get places info")
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
        val placeUiModels = mutableListOf<PlaceUiModel>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("gemini_result")

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val name = item.getString("name")
            val url = item.getString("url")
            placeUiModels.add(PlaceUiModel(name, url))
        }

        return placeUiModels
    }
}