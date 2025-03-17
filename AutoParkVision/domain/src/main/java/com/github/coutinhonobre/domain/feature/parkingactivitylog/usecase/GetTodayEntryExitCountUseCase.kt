package com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase

import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository

class GetTodayEntryExitCountUseCase(
    private val parkingActivityLogRepository: ParkingActivityLogRepository
) {
    suspend operator fun invoke() = parkingActivityLogRepository.getTodayEntryExitCount()
}