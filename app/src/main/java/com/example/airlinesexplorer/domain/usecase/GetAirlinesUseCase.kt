package com.example.airlinesexplorer.domain.usecase



import com.example.airlinesexplorer.domain.AirlineRepository
import javax.inject.Inject

class GetAirlinesUseCase @Inject constructor(
    private val repository: AirlineRepository
) {
    suspend operator fun invoke() = repository.getAirlines()
}