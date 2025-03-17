package com.github.coutinhonobre.domain.feature.vehicle.usecase

import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository

class GetFilterVehiclesUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(
        plates: List<String>
    ) = vehicleRepository.searchListVehiclePlates(plates = plates)
}