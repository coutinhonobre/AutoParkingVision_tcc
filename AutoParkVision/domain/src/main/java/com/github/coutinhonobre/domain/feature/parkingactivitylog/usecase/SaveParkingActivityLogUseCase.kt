package com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase

import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository

class SaveParkingActivityLogUseCase(
    private val parkingActivityLogRepository: ParkingActivityLogRepository
) {
    suspend operator fun invoke(
        licensePlate: String
    ): Result<String> {
        return parkingActivityLogRepository.saveLog(licensePlate)
    }
}