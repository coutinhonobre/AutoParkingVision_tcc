package com.github.coutinhonobre.domain.feature.parking.repository

import com.github.coutinhonobre.domain.feature.parking.model.Parking

interface ParkingRepository {
    suspend fun saveParking(
        parking: Parking
    ): Result<Boolean>

    suspend fun getParkingForId(): Result<Parking>

    suspend fun updateParking(
        parking: Parking
    ): Result<Boolean>
}
