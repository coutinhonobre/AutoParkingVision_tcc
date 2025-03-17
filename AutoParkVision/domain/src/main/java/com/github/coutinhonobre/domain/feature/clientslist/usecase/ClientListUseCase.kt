package com.github.coutinhonobre.domain.feature.clientslist.usecase

import com.github.coutinhonobre.domain.feature.clientslist.repository.ClientListRepository

class ClientListUseCase(
    private val repository: ClientListRepository
) {
    suspend operator fun invoke() = repository.getClientsList()
}