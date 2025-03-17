package com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase

import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository

class FilterParkingLogUseCase(
    private val parkingActivityLogRepository: ParkingActivityLogRepository
) {
    suspend operator fun invoke(
        licensePlate: String,
        startDate: Long,
        endDate: Long
    ) = parkingActivityLogRepository.getLogs(licensePlate, startDate, endDate)
}