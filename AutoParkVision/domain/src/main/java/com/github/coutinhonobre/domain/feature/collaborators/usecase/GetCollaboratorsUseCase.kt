package com.github.coutinhonobre.domain.feature.collaborators.usecase

import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository

class GetCollaboratorsUseCase(
    private val collaboratorRepository: CollaboratorRepository
) {
    suspend operator fun invoke() = collaboratorRepository.getAllCollaborators()
}