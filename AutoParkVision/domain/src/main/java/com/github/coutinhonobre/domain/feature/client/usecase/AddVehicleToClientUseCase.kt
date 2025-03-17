package com.github.coutinhonobre.domain.feature.client.usecase

import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository

class AddVehicleToClientUseCase(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(
        clientId: String,
        vehicleId: String
    ) = clientRepository.addVehicleToClient(
        clientId = clientId,
        vehicleId = vehicleId
    )
}
