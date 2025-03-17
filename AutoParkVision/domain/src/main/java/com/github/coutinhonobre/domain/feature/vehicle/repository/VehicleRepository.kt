package com.github.coutinhonobre.domain.feature.vehicle.repository

import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle

interface VehicleRepository {
    suspend fun searchAuthorizedVehicles(
        plate: String
    ): Result<List<Vehicle>> = Result.failure(Exception("Not implemented yet"))

    suspend fun getAllVehicles(): Result<List<Vehicle>> = Result.failure(Exception("Not implemented yet"))

    suspend fun saveVehicle(
        vehicle: Vehicle
    ): Result<Unit> = Result.failure(Exception("Not implemented yet"))

    suspend fun searchVehicleId(
        id: String
    ): Result<Vehicle> = Result.failure(Exception("Not implemented yet"))

    suspend fun searchListVehiclePlates(
        plates: List<String>
    ): Result<List<Vehicle>> = Result.failure(Exception("Not implemented yet"))
}