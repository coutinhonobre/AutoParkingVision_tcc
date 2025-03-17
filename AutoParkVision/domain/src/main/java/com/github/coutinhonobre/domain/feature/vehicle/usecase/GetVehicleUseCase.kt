package com.github.coutinhonobre.domain.feature.vehicle.usecase

import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository

class GetVehicleUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(id: String) = vehicleRepository.searchVehicleId(id)
}