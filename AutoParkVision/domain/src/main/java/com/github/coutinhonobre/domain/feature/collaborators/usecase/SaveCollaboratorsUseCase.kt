package com.github.coutinhonobre.domain.feature.collaborators.usecase

import com.github.coutinhonobre.domain.feature.collaborators.model.Collaborator
import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository

class SaveCollaboratorsUseCase(
    private val collaboratorRepository: CollaboratorRepository
) {
    suspend operator fun invoke(collaborator: Collaborator) = collaboratorRepository.saveCollaborator(collaborator)
}