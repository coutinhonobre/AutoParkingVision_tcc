package com.github.coutinhonobre.domain.feature.client.usecase

import com.github.coutinhonobre.domain.feature.client.model.Client
import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository

class SaveClientUseCase(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(
        client: Client
    ) = clientRepository.saveClient(
        client
    )
}