package com.example.airlinesexplorer.presentation.screens.airlines
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airlinesexplorer.domain.Airline
import com.example.airlinesexplorer.domain.AirlineRepository
import com.example.airlinesexplorer.domain.usecase.GetAirlinesUseCase
import com.example.airlinesexplorer.domain.usecase.SearchAirlinesUseCase
import com.example.airlinesexplorer.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirlinesViewModel @Inject constructor(
    private val getAirlinesUseCase: GetAirlinesUseCase,
    private val searchAirlinesUseCase: SearchAirlinesUseCase,
    private val repository: AirlineRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(AirlinesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAirlines()

        // Search functionality
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    loadAirlines()
                } else {
                    searchAirlines(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadAirlines() {
        viewModelScope.launch {
            getAirlinesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            airlines = result.data ?: emptyList(),
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

    private fun searchAirlines(query: String) {
        viewModelScope.launch {
            searchAirlinesUseCase(query).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            airlines = result.data ?: emptyList(),
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

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFavoriteClick(airlineId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(airlineId)
        }
    }

    fun onRetry() {
        loadAirlines()
    }
}

data class AirlinesUiState(
    val airlines: List<Airline> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)