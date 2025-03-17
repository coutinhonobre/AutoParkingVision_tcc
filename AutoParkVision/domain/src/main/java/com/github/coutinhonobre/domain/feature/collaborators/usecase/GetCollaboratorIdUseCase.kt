package com.github.coutinhonobre.domain.feature.collaborators.usecase

import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository

class GetCollaboratorIdUseCase(
    private val collaboratorRepository: CollaboratorRepository
) {
    suspend operator fun invoke(id: String) = collaboratorRepository.getCollaboratorById(id)
}