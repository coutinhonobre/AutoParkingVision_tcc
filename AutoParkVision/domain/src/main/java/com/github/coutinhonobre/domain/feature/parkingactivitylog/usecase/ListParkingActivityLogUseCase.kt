package com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase

import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository

class ListParkingActivityLogUseCase(
    private val parkingActivityLogRepository: ParkingActivityLogRepository
) {
    suspend operator fun invoke() = parkingActivityLogRepository.getLogs()
}