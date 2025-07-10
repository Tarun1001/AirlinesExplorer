package com.example.airlinesexplorer.domain.usecase

import com.example.airlinesexplorer.domain.AirlineRepository
import javax.inject.Inject

class SearchAirlinesUseCase @Inject constructor(
    private val repository: AirlineRepository
) {
    suspend operator fun invoke(query: String) = repository.searchAirlines(query)
}