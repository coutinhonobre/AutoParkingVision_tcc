package com.github.coutinhonobre.domain.feature.vehicle.usecase

import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository

class SearchAuthorizedVehiclesUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(plate: String): Result<List<Vehicle>> {
        return vehicleRepository.searchAuthorizedVehicles(plate)
    }
}