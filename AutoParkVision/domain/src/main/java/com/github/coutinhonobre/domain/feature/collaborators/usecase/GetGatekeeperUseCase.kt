package com.github.coutinhonobre.domain.feature.collaborators.usecase

import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository

class GetGatekeeperUseCase(
    private val collaboratorRepository: CollaboratorRepository
) {
    suspend fun validate(code: String) = collaboratorRepository.validateCode(code)
}