package com.github.coutinhonobre.domain.feature.parking.usecase

import com.github.coutinhonobre.domain.feature.parking.model.Parking
import com.github.coutinhonobre.domain.feature.parking.repository.ParkingRepository

class SaveParkingUseCase(
    private val parkingRepository: ParkingRepository
) {
    suspend operator fun invoke(parking: Parking) = parkingRepository.saveParking(parking)
}