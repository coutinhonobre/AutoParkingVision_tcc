package com.github.coutinhonobre.domain.feature.parking.usecase

import com.github.coutinhonobre.domain.feature.parking.repository.ParkingRepository

class GetParkingUseCase(
    private val parkingRepository: ParkingRepository
) {
    suspend operator fun invoke() = parkingRepository.getParkingForId()
}
