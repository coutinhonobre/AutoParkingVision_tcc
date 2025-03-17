package com.github.coutinhonobre.domain.feature.client.usecase

import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository

class GetClientUseCase(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(clientId: String) = clientRepository.getClient(clientId = clientId)
}