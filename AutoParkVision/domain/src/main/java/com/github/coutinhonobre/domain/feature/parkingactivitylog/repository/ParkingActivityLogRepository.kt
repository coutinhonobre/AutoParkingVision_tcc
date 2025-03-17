package com.github.coutinhonobre.domain.feature.parkingactivitylog.repository

import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLog
import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLogEventType

interface ParkingActivityLogRepository {
    suspend fun saveLog(
        licensePlate: String
    ) : Result<String> = Result.failure(Exception("Not implemented"))

    suspend fun getLogs() : Result<List<ParkingLog>> = Result.failure(Exception("Not implemented"))

    suspend fun getLogs(
        licensePlate: String,
        startDate: Long,
        endDate: Long
    ) : Result<List<ParkingLog>> = Result.failure(Exception("Not implemented"))

    suspend fun getTodayEntryExitCount(): Result<Pair<Int, Int>> = Result.failure(Exception("Not implemented"))
}
