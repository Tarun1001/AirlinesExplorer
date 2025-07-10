package com.example.airlinesexplorer.domain.usecase


import com.example.airlinesexplorer.domain.AirlineRepository
import javax.inject.Inject

class GetAirlineByIdUseCase @Inject constructor(
    private val repository: AirlineRepository
) {
    suspend operator fun invoke(id: String) = repository.getAirlineById(id)
}