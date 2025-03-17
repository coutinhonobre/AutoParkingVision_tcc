package com.github.coutinhonobre.domain.feature.vehicle.usecase

import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository

class SaveVehicleUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(
        vehicle: Vehicle
    ) = vehicleRepository.saveVehicle(
        vehicle = vehicle
    )
}