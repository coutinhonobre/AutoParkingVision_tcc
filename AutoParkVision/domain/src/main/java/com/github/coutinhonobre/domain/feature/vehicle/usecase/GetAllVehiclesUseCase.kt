package com.github.coutinhonobre.domain.feature.vehicle.usecase

import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository

class GetAllVehiclesUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke() = vehicleRepository.getAllVehicles()
}