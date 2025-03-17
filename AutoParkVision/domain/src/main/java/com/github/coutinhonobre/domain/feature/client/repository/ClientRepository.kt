package com.github.coutinhonobre.domain.feature.client.repository

import com.github.coutinhonobre.domain.feature.client.model.Client

interface ClientRepository {
    suspend fun saveClient(client: Client): Result<Boolean> = Result.failure(Exception("Not implemented yet"))
    suspend fun addVehicleToClient(clientId: String, vehicleId: String): Result<Boolean> = Result.failure(Exception("Not implemented yet"))
    suspend fun getClient(clientId: String): Result<Client> = Result.failure(Exception("Not implemented yet"))
}
