package com.example.airlinesexplorer.presentation.screens.details

// AirlineDetailsViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airlinesexplorer.domain.Airline
import com.example.airlinesexplorer.domain.AirlineRepository
import com.example.airlinesexplorer.domain.usecase.GetAirlineByIdUseCase
import com.example.airlinesexplorer.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirlineDetailsViewModel @Inject constructor(
    private val getAirlineByIdUseCase: GetAirlineByIdUseCase,
    private val repository: AirlineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AirlineDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadAirlineDetails(airlineId: String) {
        viewModelScope.launch {
            getAirlineByIdUseCase(airlineId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            airline = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun onFavoriteClick(airlineId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(airlineId)
            // Reload the airline to update favorite status
            loadAirlineDetails(airlineId)
        }
    }

    fun onRetry(airlineId: String) {
        loadAirlineDetails(airlineId)
    }
}

data class AirlineDetailsUiState(
    val airline: Airline? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)